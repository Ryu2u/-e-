package com.ryuzu.server.controller;


import com.ryuzu.server.domain.Employee;
import com.ryuzu.server.domain.PageRespBean;
import com.ryuzu.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/system/basic")
public class EmployeeController {

    @Resource
    private IEmployeeService employeeService;

    @ApiOperation(value = "查询所有员工(分页)")
    @GetMapping("/")
    public PageRespBean getAllEmployee(@RequestParam(defaultValue = "1") Integer pageNo,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       Employee employee,
                                       LocalDate[] beginDateScope) {
        return employeeService.getAllEmployee(pageNo,pageSize,employee,beginDateScope);
    }

}
