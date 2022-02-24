package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.Department;
import com.ryuzu.server.mapper.DepartmentMapper;
import com.ryuzu.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Resource
    public DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartment(Integer parentId) {
        return departmentMapper.getAllDepartment(parentId);

    }
}
