package com.xxy.sfl.entity;

import com.xxy.sfl.pub.annotations.Display;
import com.xxy.sfl.pub.entity.SuperEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bd_user")
@Where(clause = "dr=0")
@Display("用户实体")
public class UserEntity extends SuperEntity {

    @Display("用户编号")
    @Column(name = "user_code")
    private String userCode;

    @Display("用户名")
    @Column(name = "user_name")
    private String userName;

    @Display("密码")
    @Column(name = "password")
    private String password;

    @Display("启|停用状态")
    @Column(name = "status")
    private int status; // 0 停用 | 1启用

    @Display("用户来源")
    @Column(name = "source")
    private String source; // 商户用户记录商户主键

    @Display("角色")
    @Column(name = "role")
    private String role;// user 普通用户 | admin 1 管理员用户 | superAdmin 超级管理员

    @Display("备注")
    @Column(name = "memo")
    private String memo;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
