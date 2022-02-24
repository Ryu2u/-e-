package com.ryuzu.server.controller;


import com.ryuzu.server.domain.Department;
import com.ryuzu.server.service.IDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    @ApiOperation(value = "查询所有的部门")
    @GetMapping("/")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment(-1);

    }
}
