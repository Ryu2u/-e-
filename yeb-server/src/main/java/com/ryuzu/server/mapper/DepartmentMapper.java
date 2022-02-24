package com.ryuzu.server.mapper;

import com.ryuzu.server.domain.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface DepartmentMapper extends BaseMapper<Department> {


    /**
     * 查询所有部门
     * @return
     */
    List<Department> getAllDepartment(Integer parentId);
}
