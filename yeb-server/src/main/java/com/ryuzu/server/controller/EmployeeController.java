package com.ryuzu.server.controller;


import com.ryuzu.server.domain.*;
import com.ryuzu.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
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
@RequestMapping("/system/basic")
public class EmployeeController {

    @Resource
    private IEmployeeService employeeService;

    @Resource
    private IPoliticsStatusService politicsStatusService;

    @Resource
    private IJoblevelService joblevelService;

    @Resource
    private IPositionService positionService;

    @Resource
    private INationService nationService;

    @Resource
    private IDepartmentService departmentService;

    @ApiOperation(value = "查询所有员工(分页)")
    @GetMapping("/")
    public PageRespBean getAllEmployeeByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             Employee employee,
                                             LocalDate[] beginDateScope) {
        return employeeService.getAllEmployee(pageNo, pageSize, employee, beginDateScope);
    }

    @ApiOperation(value = "查询所有政治面貌")
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus() {
        return politicsStatusService.list();
    }

    @ApiOperation(value = "查询所有职称")
    @GetMapping("/joblevel")
    public List<Joblevel> getAllJobLevel() {
        return joblevelService.list();
    }

    @ApiOperation(value = "查询所有职位")
    @GetMapping(value = "/position")
    public List<Position> getAllPosition() {
        return positionService.list();
    }

    @ApiOperation(value = "查询所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNation() {
        return nationService.list();
    }

    @ApiOperation(value = "查询所有部门")
    @GetMapping("/department")
    public List<Department> getAllDepartment() {
        return departmentService.getAllDepartment(-1);
    }

    @ApiOperation(value = "获取最大工号")
    @GetMapping("/maxWorkID")
    public RespBean getMaxWorkID() {
        return employeeService.getMaxWordID();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean insertEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmployee(@RequestBody Employee employee) {
        if (employeeService.updateById(employee)) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmployee(@PathVariable Integer id){
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");

    }

}
