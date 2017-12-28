package com.lichi.increaselimit.security.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

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
	
	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		User user = (User)authentication.getPrincipal();
		log.info("登录成功,用户id:{}",user.getId());
		
		//判断是不是第一次登陆,如果是要去注册环信,并更新pid的转换人数
		Date updateTime = user.getUpdateTime();
		if(updateTime == null) {
			userService.registerHuanxinAndUpdatePid(user.getId(),user.getPid());
		}
		
		String token = user.getId();
		
		//将生成的token放入redis,token设置为永久
		redisUtils.set(Constants.LOGIN_USER + token, token);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("token", token);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.success(jsonObject)));
	}

}