package com.ryuzu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuzu.server.domain.MenuRole;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.mapper.MenuRoleMapper;
import com.ryuzu.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.implementation.bytecode.Throw;
import org.omg.SendingContext.RunTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Resource
    private MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public RespBean updateMenuAndRole(Integer rid, Integer[] mids) {
        List<MenuRole> ridList = menuRoleMapper.selectList(new QueryWrapper<MenuRole>().eq("rid", rid));
        Integer num = ridList.size();

        int delNum = menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (num == delNum) {
            if (mids == null || mids.length == 0) {
                return RespBean.success("更新成功!");
            }
            Integer result = menuRoleMapper.updateMenusByRid(rid, mids);
            if (result == mids.length) {
            return RespBean.success("更新成功!");
            }
        }
        throw new RuntimeException("更新失败");
    }
}
