package com.ryuzu.server.controller;

import com.ryuzu.server.domain.Admin;
import com.ryuzu.server.domain.AdminLoginParam;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录页面
 *
 * @author Ryuzu
 * @date 2022/2/18 23:01
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Resource
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) {
        return adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(),adminLoginParam.getCode(), request);

    }

    @ApiOperation(value = "获取当前用户的完整信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if (principal == null) {
            return null;
        }
        Admin admin = adminService.getAdminByUsername(principal.getName());
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout() {

        return RespBean.success("退出成功");
    }
}
