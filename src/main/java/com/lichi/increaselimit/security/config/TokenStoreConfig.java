//package com.lichi.increaselimit.security.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
//import com.lichi.increaselimit.security.jwt.JwtTokenEnhancer;
//import com.lichi.increaselimit.security.properties.SecurityProperties;
//
///**
// * token的存放配置
// * @author majie
// *
// */
//@Configuration
//public class TokenStoreConfig {
//	
//	@Autowired
//	private RedisConnectionFactory redisConnectionFactory;
//	
//	@Bean
//	@ConditionalOnProperty(prefix="lichi.oauth2Properties",name="storeType",havingValue="redis")   
//	public TokenStore redisTokenStore() {
//		return new RedisTokenStore(redisConnectionFactory);
//	}
//	
//	@Configuration
//	@ConditionalOnProperty(prefix="lichi.oauth2Properties",name="storeType",havingValue="jwt",matchIfMissing=true)   //默认是jwt
//	public static class JwtTokenConfig{
//		
//		@Autowired
//		private SecurityProperties systemProperties;
//		
//		@Bean
//		public TokenStore jwtTokenStore() {
//			return new JwtTokenStore(jwtAccessTokenConverter());
//		}
//		
//		@Bean
//		public JwtAccessTokenConverter jwtAccessTokenConverter() {
//			JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//			jwtAccessTokenConverter.setSigningKey(systemProperties.getOauth2Properties().getJwtSigningKey());
//			return jwtAccessTokenConverter;
//		}
//		
//		@Bean
//		@ConditionalOnMissingBean(name="jwtTokenEnhancer")
//		public TokenEnhancer jwtTokenEnhancer() {
//			return new JwtTokenEnhancer();
//		}
//	}
//}
