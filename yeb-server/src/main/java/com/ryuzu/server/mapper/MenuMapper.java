package com.ryuzu.server.mapper;

import com.ryuzu.server.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单列表
     *
     * @return
     */
    List<Menu> getMenuByAdminId(Integer id);

    /**
     * 根据角色获取用户列表
     * @return
     */
    List<Menu> getMenuByRole();
}
