package com.xxy.sfl.service.impl;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.exception.BusinessException;
import com.xxy.sfl.pub.service.CommonServiceImpl;
import com.xxy.sfl.pub.utils.Base64Util;
import com.xxy.sfl.repo.UserRepo;
import com.xxy.sfl.service.IUserService;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理服务接口实现类
 *
 * @author xg
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<UserRepo, UserEntity> implements IUserService {

    @Override
    public UserEntity login(HttpServletRequest request, String user, String pwd) throws BusinessException {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd)) {
            return null;
        }
        UserEntity userEntity = this.findByCode(user);
        if (userEntity == null) {
            throw new BusinessException("用户不存在!");
        }
        if (userEntity.getStatus() == 0) {
            throw new BusinessException("该用户已被停用!");
        }
        String password = Base64Util.encodeToString(userEntity.getPassword());
        if (!pwd.equals(password)) {
            throw new BusinessException("用户密码错误!");
        }
        // 保存当前登录用户到Session
        if (request != null && request.getSession() != null) {
            request.getSession().setAttribute(SESSION_USER_KEY, userEntity);
        }
        return userEntity;
    }

    @Override
    public UserEntity findByCode(String userCode) throws BusinessException {
        if (StringUtils.isEmpty(userCode)) {
            return null;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("userCode", userCode);
        List<UserEntity> result = this.findAll(condition);
        if (result == null || result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            throw new BusinessException("根据用户编号[" + userCode + "]查询到多条记录");
        } else {
            return result.get(0);
        }
    }

    @Override
    public void deleteByCode(String userCode) throws BusinessException {

    }

    @Override
    public UserEntity findBySource(String source) throws BusinessException {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("source", source);
        List<UserEntity> result = this.findAll(condition);
        if (result == null || result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            throw new BusinessException("根据用户来源[" + source + "]查询到多条记录");
        } else {
            return result.get(0);
        }
    }

    @Override
    public void deleteBySource(String source) throws BusinessException {

    }
}
