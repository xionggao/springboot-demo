package com.sb.demo.pub.exception;

import com.alibaba.fastjson.JSON;
import com.sb.demo.pub.utils.JsonBackData;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth2鉴权信息处理类
 *
 * @author xg
 */
@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        Throwable cause = e.getCause();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonBackData back = new JsonBackData();
        back.setSuccess(false);
        if (cause instanceof InvalidTokenException) {
            back.setBackMsg("认证失败: token无效");
        } else {
            back.setBackMsg("您无权访问当前资源");
        }
        response.getWriter().write(JSON.toJSONString(back));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonBackData back = new JsonBackData();
        back.setSuccess(false);
        back.setBackMsg("您无权访问当前资源");
        response.getWriter().write(JSON.toJSONString(back));
    }
}
