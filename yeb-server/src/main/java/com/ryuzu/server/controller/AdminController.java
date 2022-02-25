package com.ryuzu.server.controller;


import com.ryuzu.server.domain.Admin;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.service.IAdminRoleService;
import com.ryuzu.server.service.IAdminService;
import com.ryuzu.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IAdminRoleService adminRoleService;

    @ApiOperation(value = "查询所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmin(String keywords) {
        return adminService.getAllAdmin(keywords);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (adminService.updateById(admin)) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/role/")
    public List<Role> getAllRoles() {
        List<Role> list = roleService.list();
        return list;
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/role/")
    public RespBean updateAdmin(Integer adminId, Integer[] rids) {

        return adminRoleService.addAdmin(adminId, rids);

    }
}
