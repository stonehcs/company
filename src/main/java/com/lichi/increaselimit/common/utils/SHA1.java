package com.lichi.increaselimit.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.MultiValueMap;

/**
 * 立木征信签名算法
 * @author majie
 *
 */
public class SHA1 {
	
	public static final String APISECRET = "NOTvtxnwu9RRTKMquTzjg5N2QnLE8auG"; 
	
	public static String createSign(MultiValueMap<String, String> map, boolean encode) throws UnsupportedEncodingException {
		Set<String> keysSet = map.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		StringBuffer temp = new StringBuffer();
		boolean first = true;
		for (Object key : keys) {
			if("sign".equals(key)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				temp.append("&");
			}
			temp.append(key).append("=");
			Object value = map.get(key);
			String valueString = "";
			if (null != value) {
				valueString = String.valueOf(value);
				valueString = valueString.substring(1,valueString.length()-1);
			}
			if (encode) {
				temp.append(URLEncoder.encode(valueString, "UTF-8"));
			} else {
				temp.append(valueString);
			}
		}
		temp.append(APISECRET);
		
		System.out.println(temp);
		String sha1Hex = DigestUtils.sha1Hex(temp.toString());
		
		return sha1Hex;
	}

}