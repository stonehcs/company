package com.lichi.increaselimit.common;

/**
 * 常量
 * @author majie
 *
 */
public interface Constants {
	
	/**
	 * 手机验证码存放key
	 */
	public static final String MOBILE_REDIS_KEY = "code:sms:";
	/**
	 * 登录的客服
	 */
	public static final String LOGIN_KEFU = "login_kefu:";
	/**
	 * 登录的用户
	 */
	public static final String LOGIN_USER = "login_user:";
	/**
	 * 验证码存放在redis的时间
	 */
	public static final Long CODE_IN_REDIS_TIME = 900L;
	
	/**
	 * 隔多久重发验证码
	 */
	public static final long CODE_RESEND = 60;
}
