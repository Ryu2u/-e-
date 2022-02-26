package com.ryuzu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuzu.server.domain.Employee;
import com.ryuzu.server.domain.PageRespBean;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.mapper.EmployeeMapper;
import com.ryuzu.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取最大工号
     * @return
     */
    @Override
    public RespBean getMaxWordID() {
        //获取最大工号
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return RespBean.success(null,String.format("%08b",Integer.parseInt(maps.get(0).get("max(workID)").toString())+1));

    }
}

