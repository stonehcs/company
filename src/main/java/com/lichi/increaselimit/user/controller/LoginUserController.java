package com.lichi.increaselimit.user.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
public class LoginUserController {
	
	// 请求缓存（跳转过来之前会先把请求保存到session里面，所以下面请求可以直接从缓存中获取请求）

	private RequestCache requestCache = new HttpSessionRequestCache();

	// 重定向策略

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * 当需要身份认证时候跳转到这里 请求页面的就跳到登录页面,非页面就用ajax
	 * 
	 * @return
	 * @throws IOException
	 */
	@ApiIgnore
	@GetMapping("/authentication/require")
	public ResultVo<String> requireAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest != null) {
			String redirectUrl = savedRequest.getRedirectUrl();
			log.info("引发跳转的请求是:" + redirectUrl);
			// 是html的请求直接跳走,不是就要做验证,跳到BrowserProperties配置的路径

			if (StringUtils.endsWithIgnoreCase(redirectUrl, ".html")) {
				redirectStrategy.sendRedirect(request, response, "/login.html");
			}
		}
		return ResultVoUtil.error(401, "访问的服务需要身份认证,请引导用户到登录页"); // 这里应该封装
	}
}
