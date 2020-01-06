package com.xxy.sfl.service;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.exception.BusinessException;
import com.xxy.sfl.pub.service.ICommonService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理服务接口
 *
 * @author xg
 */
public interface IUserService extends ICommonService<UserEntity> {

    String SESSION_USER_KEY = "sfl_user";

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @param user    账号
     * @param pwd     密码
     * @return
     * @throws BusinessException
     */
    UserEntity login(HttpServletRequest request, String user, String pwd) throws BusinessException;

    /**
     * 根据用户编号查询用户信息
     *
     * @param userCode 用户编号
     * @return
     * @throws BusinessException
     */
    UserEntity findByCode(String userCode) throws BusinessException;

    /**
     * 根据用户编号逻辑删除用户
     *
     * @param userCode 用户编号
     * @throws BusinessException
     */
    void deleteByCode(String userCode) throws BusinessException;

    /**
     * 根据用户来源查询用户信息
     *
     * @param source 用户来源
     * @return
     * @throws BusinessException
     */
    UserEntity findBySource(String source) throws BusinessException;

    /**
     * 根据用户来源逻辑删除用户
     *
     * @param source 用户来源
     */
    void deleteBySource(String source) throws BusinessException;

}
