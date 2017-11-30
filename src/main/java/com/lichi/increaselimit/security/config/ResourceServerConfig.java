package com.lichi.increaselimit.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.lichi.increaselimit.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.lichi.increaselimit.security.authentication.social.authentication.OpenidAuthenticationSecurityConfig;
import com.lichi.increaselimit.security.handler.LoginFailureHandler;
import com.lichi.increaselimit.security.handler.LoginSuccessHandler;
import com.lichi.increaselimit.security.validate.code.ValidateCodeSecurityConfig;

/**
 * 资源认证服务器
 * @author majie
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Autowired
	private OpenidAuthenticationSecurityConfig socialAuthenticationSecurityConfig;
	
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.
    		formLogin()
    		.loginPage("/authentication/require") // 登陆校验权限，controller路径
    		.loginProcessingUrl("/authentication/form") // 登陆表单路径，要和页面表达路径一样
    		.successHandler(loginSuccessHandler)
    		.failureHandler(loginFailureHandler);
//    			.authorizeRequests()
//    			.antMatchers("/login.html", "/authentication/require", "/captcha-image")
//    			.permitAll();
    	
        http.apply(validateCodeSecurityConfig)
        	.and()
        	.apply(smsCodeAuthenticationSecurityConfig)
        	.and()
        	.apply(socialAuthenticationSecurityConfig)
    		.and()
                .authorizeRequests()
	                .antMatchers("/").permitAll()
	                .antMatchers("/v2/**","/swagger**", "/druid/**","/swagger-resources/**","/oauth2/client","/social/signUp").permitAll()
	                .anyRequest().permitAll()
                .and()
                .csrf().disable();
        
    }
}
