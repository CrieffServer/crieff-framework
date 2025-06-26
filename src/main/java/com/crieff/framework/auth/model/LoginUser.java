package com.crieff.framework.auth.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.crieff.framework.auth.common.enums.AccountStatus;
import com.crieff.framework.auth.common.enums.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: aKuang
 * @time: 10/19/22 4:44 PM
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    /**
     * 用户序列号
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     * @see Sex
     */
    private Integer sex;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 上次登录时间
     */
    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 账号状态
     * @see AccountStatus
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 部门主键
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色列表
     */
    private Set<Long> roleIds;

    /**
     * 角色名称列表
     */
    private Set<String> roleNames;

    /**
     * 用户权限列表
     */
    private Set<Long> permissionIds;

    /**
     * 权限名称列表
     */
    private Set<String> permissionNames;

    /**
     *  资源唯一标识列表
     */
    private Set<String> resourceUniqIds;

    /**
     * 登陆ip
     */
    private String loginIp;

    /**
     * 访问ip
     */
    private String accessIp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
