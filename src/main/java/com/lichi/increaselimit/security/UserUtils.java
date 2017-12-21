package com.lichi.increaselimit.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前用户信息
 * @author majie
 *
 */
public class UserUtils {
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public static Object getUserInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal;
	}
}
