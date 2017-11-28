package com.lichi.increaselimit.common.utils;

/**
 * 字符串的一些验证
 * @author majie
 *
 */
public class StringUtil {

	public static final String MOBILE_PATTERN = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
	
	/**
	 * 手机号码的验证
	 */
	public static boolean ValidateMobile(String mobile) {
		return mobile.matches(MOBILE_PATTERN);
	}
	
	public static void main(String[] args) {
		boolean validateMobile = ValidateMobile(null);
		System.out.println(validateMobile);
	}
}
