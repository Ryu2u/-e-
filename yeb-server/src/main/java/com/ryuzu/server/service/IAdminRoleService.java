package com.ryuzu.server.service;

import com.ryuzu.server.domain.AdminRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuzu.server.domain.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface IAdminRoleService extends IService<AdminRole> {

    RespBean addAdmin(Integer adminId, Integer[] rids);
}
