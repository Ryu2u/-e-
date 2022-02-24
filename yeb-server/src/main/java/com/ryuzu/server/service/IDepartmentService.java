package com.ryuzu.server.service;

import com.ryuzu.server.domain.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 查询所有部门
     * @return
     */
    List<Department> getAllDepartment(Integer parentId);
}
