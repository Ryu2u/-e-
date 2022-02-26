package com.ryuzu.server.service;

import com.ryuzu.server.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuzu.server.domain.PageRespBean;
import com.ryuzu.server.domain.RespBean;
import org.apache.commons.math3.analysis.function.Exp;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface IEmployeeService extends IService<Employee> {

    PageRespBean getAllEmployee(Integer pageNo, Integer pageSize, Employee employee, LocalDate[] beginDateScope);

    RespBean getMaxWordID();

    RespBean addEmployee(Employee employee);

    List<Employee> exportEmployee(Integer id);
}
