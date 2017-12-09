package com.lichi.increaselimit.common.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;

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
	
	/**
	 * 解析页面的文字
	 * @param content
	 * @return
	 */
	public static String getContentWord(String content) {
		if(StringUtils.isBlank(content)) {
			return null;
		}
		String collect = null;
		try {
			String[] substringBetween = StringUtils.substringsBetween(content, ">", "<");
					Arrays.asList(substringBetween).stream().collect(Collectors.joining());
		}catch (Exception e) {
			throw new BusinessException(ResultEnum.CONTENT_ERRO);
		}
		return StringUtils.substring(collect, 0, 30) + "...";
	}
	
	public static void main(String[] args) {
		boolean validateMobile = ValidateMobile(null);
		System.out.println(validateMobile);
	}
}
