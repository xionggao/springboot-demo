package com.xxy.sfl.pub.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xxy.sfl.pub.utils.JsonBackData;

/**
 * 全局异常处理器
 * 
 * @author xg
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public JsonBackData exceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String queryString = request.getQueryString();
		logger.error(String.format("请求地址:%s,请求方式:%s,请求参数:%s", uri, method, queryString), e);
		JsonBackData back = new JsonBackData();
		back.setSuccess(false);
		back.setBackMsg(e.getMessage());
		return back;
	}
}
