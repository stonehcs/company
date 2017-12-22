package com.lichi.increaselimit.security.servie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import com.lichi.increaselimit.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 重写,通过用户名获取用户信息
 * 
 * @author majie
 *
 */
@Slf4j
@Component
public class MyUserDetailsServiceImpl implements UserDetailsService,SocialUserDetailsService {


	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {

		log.info("手机号登录,手机号码为:" + mobile);
		return userService.loadUserInfoByMobile(mobile);
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		log.info("第三方登录,用户登录:" + userId);
		return userService.loadUserInfoByUserId(userId);
	}

}
