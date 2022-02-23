package com.ryuzu.server.service.impl;

import com.ryuzu.server.domain.Admin;
import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.mapper.MenuMapper;
import com.ryuzu.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.baomidou.mybatisplus.core.toolkit.IdWorker.getId;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Menu> getMenuByAdminId() {
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        List<Menu> menuList ;
        menuList = (List<Menu>) ops.get("menu_" + adminId);
        if (menuList == null) {
            List<Menu> menuByAdminId = menuMapper.getMenuByAdminId(adminId);
            if (menuByAdminId == null) {
                ops.set("menu_" + adminId, new ArrayList<>(),30, TimeUnit.SECONDS);
                return new ArrayList<>();
            }
            ops.set("menu_" + adminId, menuByAdminId);
            return menuByAdminId;
        }
        return menuList;
    }

    /**
     * 根据角色获取用户列表
     * @return
     */
    @Override
    public List<Menu> getMenuByRole() {
        return menuMapper.getMenuByRole();
    }
}
