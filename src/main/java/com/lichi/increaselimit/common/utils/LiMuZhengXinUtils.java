package com.lichi.increaselimit.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * 立木征信utils
 * @author majie
 *
 */
public class LiMuZhengXinUtils {

	public final static String APIKEY = "0493706513949334";
	private final static String APISECRET = "NOTvtxnwu9RRTKMquTzjg5N2QnLE8auG";

	private final static String GETTOKEN_URL = "https://api.limuzhengxin.com/api/gateway";
	public final static String TEST_URL = "https://t.limuzhengxin.cn/api/gateway";

	/**
	 * 发送post请求获取token
	 * @param restTemplate
	 * @return
	 */
	public static JSONObject doPostForToken(RestTemplate restTemplate,MultiValueMap<String, String> map) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		
		JSONObject postForObject = restTemplate.postForObject(TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	/**
	 * 获取信息
	 * @param restTemplate
	 * @param method
	 * @param bizType
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject getInfo(RestTemplate restTemplate,String method, String bizType, String token) throws UnsupportedEncodingException {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", APIKEY);
		map.add("version", "1.2.0");
		map.add("bizType", bizType);
		map.add("token", token);
		String sign = createSign(map, false);
		map.add("sign", sign);
		JSONObject postForObject = restTemplate.postForObject(TEST_URL, map, JSONObject.class);
		return postForObject;
	}
	
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
