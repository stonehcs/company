package com.lichi.increaselimit.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichi.increaselimit.common.utils.IdUtils;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.user.entity.User;

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

		User user = (User)authentication.getPrincipal();
		
		String token = IdUtils.getUUID();
		
		//将生成的token放入redis,token设置为永久
		redisUtils.set("login_user:" + token, JSONObject.toJSONString(user));
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.success(user)));
	}

}