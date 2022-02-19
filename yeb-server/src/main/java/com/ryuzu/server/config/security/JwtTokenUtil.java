package com.ryuzu.server.config.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryuzu
 * @date 2022/2/18 14:48
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户信息生成Token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        // 生成载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);

    }

    /**
     * 验证token
     * 用户是否一致,是否过期
     *
     * @return
     */
    public boolean verityToken(String token,UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        boolean flag = false;
        if (username.equals(userDetails.getUsername())&& verityTokenIsExpiration(token)){
            //验证通过
            flag =true;
        }
        return flag;
    }

    /**
     * 验证token 是否可以被刷新
     *
     * @return
     */
    public boolean canRefresh(String refreshToken) {
        return verityTokenIsExpiration(refreshToken);
    }

    /**
     * 刷新token
     *
     * @return
     */
    public String refreshToken(String refreshToken) {

        Claims claims = getClaimsFromToken(refreshToken);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);

    }


    /**
     * 根据载荷生成jwt token
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();

    }


    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username=null;
            e.printStackTrace();
        }
        return username;
    }



    /**
     * 验证token 是否过期
     * @param token
     * @return true  没过期
     *         false 已过期
     */
    private boolean verityTokenIsExpiration(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        Date nowTime = new Date();
        if (expiration.before(nowTime)) {
            //过期了
            return false;
        }else{
            return true;
        }
    }



    /**
     * 从token中获取Claims
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims= null;
            e.printStackTrace();
        }
        return claims;


    }


    /**
     * 生成Token 失效时间
     * 为当前时间加失效时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        // expiration 的单位为秒
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


}
