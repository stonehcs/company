package com.lichi.increaselimit.user.controller;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.security.properties.SecurityProperties;
import com.lichi.increaselimit.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 测试controller
 * 
 * @author majie
 *
 */
@Api(description = "用户信息")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private SecurityProperties systemProperties;

	/**
	 * 获取当前用户信息
	 * 解析jwt的token
	 */
	@GetMapping("/me")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "string", paramType = "header",defaultValue="bearer ")})
	public ResultVo<User> getCurrentUser(Authentication authentication, HttpServletRequest request)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {
		String header = request.getHeader("Authorization");
		
		String token = StringUtils.substringAfter(header, "bearer ");

		Claims claims = Jwts.parser()
				.setSigningKey(systemProperties.getOauth2Properties().getJwtSigningKey().getBytes("UTF-8"))
				.parseClaimsJws(token).getBody();
		
		return ResultVoUtil.success((User)claims.get("user"));
	}

}
