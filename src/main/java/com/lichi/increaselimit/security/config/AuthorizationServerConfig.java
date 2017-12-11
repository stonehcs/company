//package com.lichi.increaselimit.security.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import com.lichi.increaselimit.security.properties.Oauth2ClientProperties;
//import com.lichi.increaselimit.security.properties.SecurityProperties;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 认证服务器
// * 
// * @author majie
// *
// */
//@Configuration
//@EnableAuthorizationServer
//@Slf4j
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//	@Autowired
//	private SecurityProperties systemProperties;
//
//	@Autowired
//	private TokenStore tokenStore;
//
//	@Autowired(required = false)
//	private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//	@Autowired(required = false)
//	private TokenEnhancer jwtTokenEnhancer;
//
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//
//		endpoints.tokenStore(tokenStore) // 启用自己配置的tokenstore
//				.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
//
//		if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
//			log.info("-------------------使用的是jwt--------------------");
//
//			TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//			List<TokenEnhancer> enhancers = new ArrayList<>();
//			enhancers.add(jwtTokenEnhancer);
//			enhancers.add(jwtAccessTokenConverter);
//			enhancerChain.setTokenEnhancers(enhancers);
//
//			endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
//		}
//
//	}
//
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//
//		if (ArrayUtils.isNotEmpty(systemProperties.getOauth2Properties().getClients())) {
//
//			for (Oauth2ClientProperties oauth2ClientProperties : systemProperties.getOauth2Properties().getClients()) {
//				builder.withClient(oauth2ClientProperties.getClientId())
//						.secret(oauth2ClientProperties.getClientSecret())
//						.accessTokenValiditySeconds(oauth2ClientProperties.getAccessTokenValiditySeconds())
//						.authorizedGrantTypes(oauth2ClientProperties.getGrantTypes())   //有四种模式
//						.scopes(oauth2ClientProperties.getScopes());
//			}
//		}
//	}
//
//}
