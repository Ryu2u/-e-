package com.ryuzu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuzu.server.config.security.JwtTokenUtil;
import com.ryuzu.server.domain.Admin;
import com.ryuzu.server.domain.Menu;
import com.ryuzu.server.domain.RespBean;
import com.ryuzu.server.domain.Role;
import com.ryuzu.server.mapper.AdminMapper;
import com.ryuzu.server.mapper.RoleMapper;
import com.ryuzu.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuzu.server.util.AdminUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ryuzu
 * @since 2022-02-14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private RoleMapper roleMapper;



    /**
     * 登录验证并生成token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password,String code, HttpServletRequest request) {
        //验证 验证码是否正确
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (captcha == null || !code.equalsIgnoreCase(captcha)) {
            return RespBean.error("验证码错误,请重新输入");
        }
        // 根据用户名获取userDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            // 用户为空,或者密码错误
            return RespBean.error("用户名不存在或密码错误");

        } else if (!userDetails.isEnabled()) {
            //账户不可用
            return RespBean.error("账户已被冻结,请联系管理员");
        }

        // 更新security对象 第一个参数是userDetails, 第二个参数是凭证(密码)一般为null ,第三个参数是用户的一个权限
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        // 将这个对象放入spring security的全局变量里 说明用户已登录
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        //登录成功,生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHeader", tokenHead);

        return RespBean.success("登录成功", tokenMap);
    }


    @Override
    public Admin getAdminByUsername(String username) {

        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
        if (admin == null) {

            return new Admin();
        }

        return admin;
    }

    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {

        List<Role> roles = roleMapper.getRolesByAdminId(adminId);

        return roles;
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmin(String keywords) {
        Admin admin = AdminUtils.getCurrentAdmin();
        Integer adminId = admin.getId();
        List<Admin> allAdmin = adminMapper.getAllAdmin(adminId, keywords);
        return allAdmin;
    }
}
