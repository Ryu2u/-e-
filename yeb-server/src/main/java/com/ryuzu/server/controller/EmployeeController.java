package com.ryuzu.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportExcelItem;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.ryuzu.server.domain.*;
import com.ryuzu.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.util.URLUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public RespBean deleteEmployee(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");

    }

    @ApiOperation(value = "导出员工表excel表格")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployee(Integer id, HttpServletResponse response) {
        List<Employee> employees = employeeService.exportEmployee(id);
        // HSSF表示03版本的excel XSSF 表示07版本的excel
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, employees);
        ServletOutputStream out = null;
        try {
            //流形式
            response.setHeader("Content-Type", "application/octet-stream");
            //防止乱码
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("员工表.xls", "utf-8"));
            out = response.getOutputStream();
            workbook.write(out);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 注: 导入excel 时请求方式必须为post
    @ApiOperation(value = "导入excel员工表")
    @PostMapping("/import")
    public RespBean importExcel( MultipartFile file) {
        ImportParams params = new ImportParams();
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        params.setTitleRows(1);
        try {
            List<Employee> employees = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            employees.forEach(employee -> {
                //设置民族id
                employee.setNationId(nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setPoliticId(politicsStatusList
                        .get(politicsStatusList
                                .indexOf(new PoliticsStatus(employee
                                        .getPoliticsStatus()
                                        .getName())))
                        .getId());
                // 设置部门id
                employee.setDepartmentId(departmentList
                        .get(departmentList
                                .indexOf(new Department(employee
                                        .getDepartment()
                                        .getName())))
                        .getId());
                //设置职称id
                employee.setJobLevelId(joblevelList
                        .get(joblevelList
                                .indexOf(new Joblevel(employee
                                        .getJoblevel()
                                        .getName())))
                        .getId());
                // 设置职位
                employee.setPosId(positionList
                        .get(positionList
                                .indexOf(new Position(employee
                                        .getPosition()
                                        .getName())))
                        .getId());

            });
            if (employeeService.saveBatch(employees)) {
                return RespBean.success("导入成功!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败!");

    }
}
