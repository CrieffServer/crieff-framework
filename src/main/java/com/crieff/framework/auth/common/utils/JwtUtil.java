package com.crieff.framework.auth.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.crieff.framework.auth.model.JwtContent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * @description:
 * @author: aKuang
 * @time: 10/18/22 5:33 PM
 */
@Slf4j
@Component
public class JwtUtil {


    @Value("${token.expireTime:86400}")
    private Long expireTime;


    @Value("${token.secret:aB9lM7xWt8zR2uQv}")
    private String secret;


    /**
     * 构建 jwt token串
     *
     * @param jwtContent
     * @return String
     */
    public String generateToken(JwtContent jwtContent) {
        Date nowDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + expireTime * 1000L);
        /*
         * jwt的头部承载两部分信息：
         * 声明类型，这里是jwt
         * 声明加密的算法 通常直接使用 HMAC SHA256
         * {
         *   'typ': 'JWT',
         *   'alg': 'HS256'
         * }
         *
         * playload
         * 载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分
         * 标准中注册的声明
         * 公共的声明
         * 私有的声明
         *
         * signature
         * jwt的第三部分是一个签证信息，这个签证信息由三部分组成：
         * header (base64后的)
         * payload (base64后的)
         * secret 私钥
         */
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(JSON.toJSONString(jwtContent))
                .setIssuedAt(nowDate)   //设置生成 token 的时间
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * 获取凭证信息
     *
     * @param token jwt token串
     * @return Claims
     */
    public Claims getClaimByToken(String token) {
        try {
            if (StringUtils.startsWithIgnoreCase(token, "Bearer ")) {
                token = token.split(" ")[1];
            }
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("[getClaimByToken]:token 凭证信息有误! {}", e.getMessage());
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authorization = request.getHeader("Authorization");
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            log.error("authorization==>" + authorization + ", url==>" + url + ", uri==>" + uri);
            return null;
        }
    }


    /**
     * 获取过期时间
     *
     * @param token jwt token 串
     * @return Date
     */
    public Date getExpiration(String token) {
        return getClaimByToken(token).getExpiration();
    }


    /**
     * 验证token是否失效
     *
     * @param token token
     * @return true:过期   false:没过期
     */
    public boolean isExpired(String token) {
        try {
            final Date expiration = getExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("[JwtUtils --> isExpired]: {}", e.getMessage());
            return true;
        }
    }


    /**
     * 检验是否为 jwt 格式的字符串
     * <p>
     * 说明: jwt 字符串由三部分组成, 分别用 . 分隔开, 所以认为有两个 . 的字符串就是jwt形式的字符串
     *
     * @param token jwt token串
     * @return boolean
     */
    public boolean isJwtStr(String token) {
        return StringUtils.countMatches(token, '.') == 2;
    }


    /**
     * 获取 jwt 中的账户名
     *
     * @param token jwt token 串
     * @return String
     */
    public String getAccountName(String token) {
        String subject = getClaimByToken(token).getSubject();
        JwtContent jwtContent = JSONObject.parseObject(subject, JwtContent.class);
        return jwtContent.getUsername();
    }


    /**
     * 获取 jwt 的账户对象
     *
     * @param token
     * @return
     */
    public JwtContent getTokenSubjectObject(String token) {
        Claims claimByToken = getClaimByToken(token);
        String subject = claimByToken.getSubject();
        String body = JSONObject.toJSONString(subject);
        Object parse = JSON.parse(body);
        String s = parse.toString();
        return JSONObject.parseObject(s, JwtContent.class);
    }


    /**
     * 获取 jwt 账户信息的json字符串
     *
     * @param token
     * @return
     */
    public String getTokenSubjectStr(String token) {
        String body = JSONObject.toJSONString(getClaimByToken(token).getSubject());
        Object parse = JSON.parse(body);
        return parse.toString();
    }

    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.expireTime = 10L;//10秒过期
        jwtUtil.secret = "godNan";// 随便字符串  加密盐
        JwtContent content = new JwtContent();//自定义的实体类 用户信息 存储到jwt里
        content.setUserName("张三");
        String token = jwtUtil.generateToken(content);
        System.out.println("生成的token是" + token);
        boolean flag = jwtUtil.isExpired(token);//token 是否有效？
        System.out.println("token是否过期" + flag);

        if (!flag) {
            //如果没有过期那我们获取里面的信息
            JwtContent content1 = jwtUtil.getTokenSubjectObject(token);
            System.out.println(content1.toString());
        }
        try {
            Thread.sleep(15000);//休眠15s，token一定过期，因为token有效期 是10s
        } catch (Exception e) {
            log.error("", e);
        }
        flag = jwtUtil.isExpired(token);//token 是否有效？
        System.out.println("token是否过期" + flag);
    }
}