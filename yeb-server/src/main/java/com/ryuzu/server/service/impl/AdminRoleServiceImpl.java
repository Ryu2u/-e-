package com.ryuzu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuzu.server.domain.AdminRole;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.mapper.AdminRoleMapper;
import com.ryuzu.server.service.IAdminRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    @Transactional
    public RespBean addAdmin(Integer adminId, Integer[] rids) {

        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));

        Integer result = adminRoleMapper.addAdmin(adminId, rids);
        if (result == rids.length) {
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
