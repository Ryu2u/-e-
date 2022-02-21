package com.ryuzu.server.service;

import com.ryuzu.server.domain.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.RespBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password,String code, HttpServletRequest request);

    /**
     * 根据用户名返回完整的用户信息
     * @param username
     * @return
     */
    Admin getAdminByUsername(String username);


}
