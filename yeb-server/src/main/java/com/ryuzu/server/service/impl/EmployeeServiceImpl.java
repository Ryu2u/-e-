package com.ryuzu.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuzu.server.domain.Employee;
import com.ryuzu.server.domain.PageRespBean;
import com.ryuzu.server.mapper.EmployeeMapper;
import com.ryuzu.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public PageRespBean getAllEmployee(Integer pageNo, Integer pageSize, Employee employee, LocalDate[] beginDateScope) {
        //开启分页
        Page<Employee> page = new Page<>(pageNo,pageSize);
        IPage<Employee> iPage =  employeeMapper.getALlEmployee(page,employee,beginDateScope);
        PageRespBean pageRespBean = new PageRespBean(iPage.getTotal(),iPage.getRecords());
        return pageRespBean;
    }
}

