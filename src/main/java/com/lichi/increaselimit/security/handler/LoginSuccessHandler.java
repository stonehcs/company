package com.lichi.increaselimit.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.utils.UserIdUtils;
import com.lichi.increaselimit.security.UserUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功处理
 * 创建oauth2.0token
 * @author majie
 *
 */
@Component("loginSuccessHandler")
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RedisUtils redisUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		log.info("登录成功");

		String mobile = request.getParameter("mobile");
		String openid = request.getParameter("openid");
		
		String username = StringUtils.isBlank(mobile) ? openid : mobile;
		//将生成的token放入redis
		redisUtils.set("login_token:" + username, UserIdUtils.getUserId(), 7200);
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.success(UserUtils.getUserInfo())));
	}

}