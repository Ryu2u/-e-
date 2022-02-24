package com.ryuzu.server.service;

import com.ryuzu.server.domain.MenuRole;
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
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenuAndRole(Integer rid, Integer[] mids);
}
