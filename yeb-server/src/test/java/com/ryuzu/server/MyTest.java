package com.ryuzu.server;

import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.service.IMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ryuzu
 * @date 2022/2/14 20:40
 */
@SpringBootTest
public class MyTest {

    @Resource
    private IMenuService menuService;


    @Test
    public void test01(){
        List<Menu> menuByRole = menuService.getMenuByRole();
        for (Menu menu : menuByRole) {
            List<Role> roles = menu.getRoles();
            List<SimpleGrantedAuthority> collect = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
            for (SimpleGrantedAuthority simpleGrantedAuthority : collect) {
                System.out.println(simpleGrantedAuthority);
            }

        }

    }

    @Test
    public void test02(){
        List<Menu> menuByRole = menuService.getMenuByRole();
        for (Menu menu : menuByRole) {
            List<Role> roles = menu.getRoles();
            List<String> collect = roles.stream().map(Role::getName).collect(Collectors.toList());
            for (String s : collect) {
                System.out.println(s);
            }

        }

    }

}
