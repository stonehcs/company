//package com.lichi.increaselimit.common.config;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.alibaba.fastjson.JSONObject;
//import com.lichi.increaselimit.common.utils.RedisUtils;
//import com.lichi.increaselimit.common.utils.ResultVoUtil;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 登录过滤器
// * 
// * @author majie
// *
// */
//@Component
//@Slf4j
//public class TokenLoginFilter extends OncePerRequestFilter implements InitializingBean {
//
//	@Autowired
//	private RedisUtils redisUtils;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws ServletException, IOException {
//		String username = request.getHeader("token");
//		if (!StringUtils.isBlank(username)) {
//			log.info("登录用户:" + username);
//			String tokenJson = redisUtils.get("login_token:" + username);
//			if (StringUtils.isBlank(tokenJson)) {
//				response.setContentType("application/json;charset=UTF-8");
//				response.getWriter().write(JSONObject
//						.toJSONString(ResultVoUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "token失效")));
//				return;
//			}
//			
//			// 将生成的token放入redis
//			redisUtils.set("login_token:" + username, tokenJson, 7200);
//			
//		}
//		chain.doFilter(request, response);
//
//	}
//
//	
//}