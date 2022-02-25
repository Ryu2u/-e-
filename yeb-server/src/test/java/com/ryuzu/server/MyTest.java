package com.ryuzu.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.MenuRole;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.mapper.MenuRoleMapper;
import com.ryuzu.server.service.IMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Resource
    private MenuRoleMapper menuRoleMapper;


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

    @Test
    public void test03(){
        List<MenuRole> rid = menuRoleMapper.selectList(new QueryWrapper<MenuRole>().eq("rid", 4));
        System.out.println(rid.size());
        rid.forEach(System.out::println);

    }

    @Test
    public void test04(){
        LocalDate parse = LocalDate.parse("2012-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(parse);

    }


}
