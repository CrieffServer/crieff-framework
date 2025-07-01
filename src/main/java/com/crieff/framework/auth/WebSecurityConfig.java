package com.crieff.framework.auth;

import com.crieff.framework.auth.filter.JwtAuthenticationTokenFilter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


/**
 * @description:
 * @author: aKuang
 * @time: 10/9/22 5:47 PM
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    /**
     * 认证成功结果处理器
     */
    private final AuthenticationSuccessHandler loginSuccessHandler;
    /**
     * 认证失败结果处理器
     */
    private final AuthenticationEntryPoint loginFailureHandler;
    /**
     * 退出处理类
     */
    private final LogoutSuccessHandler loginLogoutSuccessHandler;
    /**
     * 用户信息服务
     */
    private final UserDetailsService userDetailsService;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //将编写的UserDetailsService注入进来
        provider.setUserDetailsService(userDetailsService);
        //  将使用的密码编译器加入进来
        provider.setPasswordEncoder(passwordEncoder);
        //将provider放置到AuthenticationManager 中
        return new ProviderManager(provider);
    }

    /**
     * 指定加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt加密密码
        return new BCryptPasswordEncoder();
    }


    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 允许当前网页仅被同源（相同协议、域名、端口）的页面嵌入到 <iframe> 中，拒绝其他来源的嵌入请求。
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 禁用basic明文验证
                .httpBasic(Customizer.withDefaults())
                // 基于token不需要 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用默认登出页
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessHandler(loginLogoutSuccessHandler))
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 设置处理鉴权失败、认证失败
                .exceptionHandling(exception -> exception.authenticationEntryPoint(loginFailureHandler))
                // 下面开始设置权限
                .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
                        // 允许直接访问 授权登录接口
                        .requestMatchers("/crieff/admin/access/control/**").permitAll()
                        // 允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated()
                )
                //  添加过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}

