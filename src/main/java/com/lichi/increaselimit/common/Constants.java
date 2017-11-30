package com.lichi.increaselimit.common;

/**
 * 常亮
 * @author majie
 *
 */
public interface Constants {
	
	/**
	 * 手机验证码存放key
	 */
	public static final String MOBILE_REDIS_KEY = "code:sms:";
	/**
	 * 验证码存放在redis的时间
	 */
	public static final Long CODE_IN_REDIS_TIME = 60L;
}
