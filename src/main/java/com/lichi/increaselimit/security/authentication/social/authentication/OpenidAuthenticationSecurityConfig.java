package com.lichi.increaselimit.security.authentication.social.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import com.lichi.increaselimit.security.handler.LoginFailureHandler;
import com.lichi.increaselimit.security.handler.LoginSuccessHandler;
import com.lichi.increaselimit.user.service.UserService;

/**
 * 短信认证安全配置
 * 注入provider需要的配置
 * @author majie
 *
 */
@Component
public class OpenidAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	
	@Autowired
	private SocialUserDetailsService userDetailsService;
	
	@Autowired
	private UsersConnectionRepository usersConnectionRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		OpenidAuthenticationFilter socialAuthenticationFilter = new OpenidAuthenticationFilter();
		socialAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		socialAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		socialAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
		
		OpenidAuthenticationProvider socialAuthenticationProvider = new OpenidAuthenticationProvider();
		socialAuthenticationProvider.setSocialUserDetailsService(userDetailsService);
		socialAuthenticationProvider.setUsersConnectionRepository(usersConnectionRepository);
		socialAuthenticationProvider.setUserService(userService);
		http.authenticationProvider(socialAuthenticationProvider)
			.addFilterAfter(socialAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
