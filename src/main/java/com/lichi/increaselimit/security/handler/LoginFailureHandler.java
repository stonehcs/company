package com.lichi.increaselimit.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichi.increaselimit.common.utils.ResultVoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录失败处理器
 * @author majie
 *
 */
@Component("loginFailureHandler")
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if(exception instanceof InternalAuthenticationServiceException) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage())));
			return;
		}
		log.info("登录认证失败,手机号为:{}",request.getParameter("mobile"));
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage())));
	}

}