package com.sb.demo.pub.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sb.demo.pub.utils.JsonBackData;

/**
 * 全局异常处理器
 * 
 * @author xg
 * @date 2019-10-23
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public JsonBackData exceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String queryString = request.getQueryString();
		logger.error(String.format("请求地址:%s,请求方式:%s,请求参数:%s", uri, method, queryString), e);
		JsonBackData back = new JsonBackData();
		back.setSuccess(false);
		back.setBackMsg("异常:" + e.getMessage());
		return back;
	}
}
