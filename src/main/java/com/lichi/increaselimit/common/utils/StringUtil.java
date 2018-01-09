package com.lichi.increaselimit.common.utils;

import java.time.LocalDate;
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

	public static final String MOBILE_PATTERN = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,3,6,7,8])|(19[7]))\\d{8}$";
	
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
	
	public static String dateFormat(String date) {
		String[] split = date.split("-");
		int year = Integer.parseInt(split[0]);
		int month = Integer.parseInt(split[1]);
		int day = Integer.parseInt(split[2]);
		
		date = LocalDate.of(year, month, day).toString();
		return date;
	}
	
	public static void main(String[] args) {
		boolean validateMobile = ValidateMobile("17382273148");
		System.out.println(validateMobile);
	}
}
