package com.ryuzu.server.config.security;


import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.service.IMenuService;

import org.springframework.security.access.ConfigAttribute;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 根据用户请求的url查询用户所需要的权限
 *
 * @author Ryuzu
 * @date 2022/2/23 16:20
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Resource
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menuByRole = menuService.getMenuByRole();
        for (Menu menu : menuByRole) {

            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                String[] roleList = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(roleList);
            }

        }

        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
