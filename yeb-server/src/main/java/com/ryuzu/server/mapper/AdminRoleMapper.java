package com.ryuzu.server.mapper;

import com.ryuzu.server.domain.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer addAdmin(Integer adminId, Integer[] rids);
}
