package com.lichi.increaselimit.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.lichi.increaselimit.user.entity.User;

/**
 * 获取当前用户信息
 * @author majie
 *
 */
public class UserUtils {
	
	/**
	 * 获取当前用户名
	 * @return
	 */
	public static User getUserInfo() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
}
