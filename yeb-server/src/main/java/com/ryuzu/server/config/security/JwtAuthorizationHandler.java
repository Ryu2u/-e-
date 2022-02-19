package com.ryuzu.server.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录授权过滤器
 *
 * @author Ryuzu
 * @date 2022/2/19 14:19
 */
public class JwtAuthorizationHandler extends OncePerRequestFilter {

    // Authorization
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    // Bearer
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private JwtTokenUtil jwtTokenUtil;


    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(tokenHeader);
        // 判断header 是否存在, 是否是以tokenHead 开头的
        if (header != null || header.startsWith(tokenHead)) {
            // 头部有效
            String token = header.substring(tokenHead.length() + 1);

            String username = jwtTokenUtil.getUserNameFromToken(token);
            // 存在用户名但未登录
            if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证token 是否合法
                if (jwtTokenUtil.verityToken(token, userDetails)) {
                    //重新设置用户对象
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    //filterChain.doFilter(httpServletRequest, httpServletResponse);

                }

            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

}
