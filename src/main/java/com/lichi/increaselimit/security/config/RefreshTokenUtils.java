//package com.lichi.increaselimit.security.config;
//
//import java.io.IOException;
//import java.util.HashMap;
//
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//
///**
// * 刷新token
// * @author majie
// *
// */
//public class RefreshTokenUtils {
//	
//	private static final String LICHI = "lichi";
//	
//	public static OAuth2AccessToken refreshToken(AuthorizationServerTokenServices authorizationServerTokenServices,
//			ClientDetailsService clientDetailsService,
//			String refreshToken) throws IOException {
//		
//		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(LICHI);
//		
//		//创建tokenRequest,custom自定义
//		TokenRequest tokenRequest = new TokenRequest(new HashMap<String, String>(), LICHI, clientDetails.getScope(), "custom");
//		OAuth2AccessToken refreshAccessToken = authorizationServerTokenServices.refreshAccessToken(refreshToken, tokenRequest);
//		return refreshAccessToken;
//	}
//	
//}
