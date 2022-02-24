package com.ryuzu.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.MenuRole;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.service.IMenuRoleService;
import com.ryuzu.server.service.IMenuService;
import com.ryuzu.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组控制器
 * declare concat
 * @author Ryuzu
 * @date 2022/2/24 14:37
 */
@RestController
@RequestMapping("/system/basic/permission")
public class PermissionController {

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;

    @Resource
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRole() {
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        if (!role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }

        if (roleService.save(role)) {
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid) {
        if (roleService.removeById(rid)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/menu/{rid}")
    public List<Integer> getMenuIdById(@PathVariable Integer rid) {
        List<MenuRole> menuIdList = menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid));
        List<Integer> idList = menuIdList.stream().map(MenuRole::getMid).collect(Collectors.toList());
        return idList;
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/menu/update")
    public RespBean updateMenuAndRole(Integer rid,Integer [] mids){
        return menuRoleService.updateMenuAndRole(rid, mids);
    }
}
