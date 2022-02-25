package com.ryuzu.server.mapper;

import com.ryuzu.server.domain.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuzu.server.domain.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface AdminMapper extends BaseMapper<Admin> {


    /**
     * 获取所有操作员
     * @param adminId
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmin(Integer adminId, String keywords);
}
