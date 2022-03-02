package com.ryuzu.server.config.security;

import com.ryuzu.server.domain.Admin;
import com.ryuzu.server.service.IAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 *
 * @author Ryuzu
 * @date 2022/2/19 13:21
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Resource
    private IAdminService adminService;

    @Resource
    private RestfulAccessDeniedHandler accessDeniedHandler;

    @Resource
    private RestAuthorizationEntryPoint authorizationEntryPoint;

    @Resource
    private CustomFilter customFilter;

    @Resource
    private CustomUrlDecisionFilter customUrlDecisionFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Admin admin = adminService.getAdminByUsername(username);
            if (admin == null) {
                return null;
            }else{
                admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
                return admin;
            }
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/doc.html","/webjars/**","/css/**","/js/**","/swagger-resources/**","/v2/api-docs/**","/captcha");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用jwt不需要csrf
        http.csrf()
                .disable()
                //基于token 不需要session
                .sessionManagement()
                //STATELESS 表示Spring Security 永远不会创建session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login","/logout")
                .permitAll()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionFilter);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthorizationHandler(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authorizationEntryPoint);

    }



    @Bean
    public JwtAuthorizationHandler jwtAuthorizationHandler(){
        return new JwtAuthorizationHandler();
    }
}
