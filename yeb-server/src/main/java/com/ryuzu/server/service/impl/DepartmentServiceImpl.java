package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.Department;
import com.ryuzu.server.domain.RespBean;
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

    @Override
    public void addDepartment(Department department) {
        departmentMapper.addDepartment(department);
    }

    @Override
    public RespBean deleteDepartment(Integer id) {
        Department dept = new Department();
        dept.setId(id);
        departmentMapper.deleteDepartment(dept);
        if (dept.getResult() == 1) {
            return RespBean.success("删除成功!");
        }else if (dept.getResult() == -2){
            return RespBean.error("删除失败,当前部门下有子部门");
        } else if (dept.getResult() == -1) {
            return RespBean.error("删除失败,当前部门下有仍有员工");
        }
        return RespBean.error("删除失败!");
    }
}
