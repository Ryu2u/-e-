package com.ryuzu.server.service;

import com.ryuzu.server.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuzu.server.domain.PageRespBean;
import com.ryuzu.server.domain.RespBean;

import java.time.LocalDate;

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
}
