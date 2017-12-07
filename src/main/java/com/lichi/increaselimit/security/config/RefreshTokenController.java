package com.lichi.increaselimit.security.config;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 刷新token
 * @author majie
 *
 */
@Api(description = "刷新token")
@RestController
public class RefreshTokenController {
	
	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;
	
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@GetMapping("/refresh-token")
	@ApiOperation("刷新token,要带请求头信息")
	public Object refreshToken(HttpServletRequest request) throws IOException {
		
		String token = request.getParameter("token");
		
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Basic ")) {
			throw new UnapprovedClientAuthenticationException("请求头中无clientId信息");
		}

		String[] tokens = extractAndDecodeHeader(header, request);
		assert tokens.length == 2;

		String clientId = tokens[0];
		String clientSecret = tokens[1];
		
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
		
		//校验clientDetails
		if(clientDetails == null ) {
			throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在：" + clientId);
		}else if(!clientSecret.equals(clientDetails.getClientSecret())) {
			throw new UnapprovedClientAuthenticationException("clientIdSecret不匹配：" + clientId);
		}
		//创建tokenRequest,custom自定义
		TokenRequest tokenRequest = new TokenRequest(new HashMap<String, String>(), clientId, clientDetails.getScope(), "custom");
		OAuth2AccessToken refreshAccessToken = authorizationServerTokenServices.refreshAccessToken(token, tokenRequest);
		return refreshAccessToken;
	}
	
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}
}
