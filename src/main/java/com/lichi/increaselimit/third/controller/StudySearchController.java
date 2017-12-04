package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.BASE64Utils;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.SHA1;

import lombok.extern.slf4j.Slf4j;

/**
 * 立木征信 学历查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/limu")
@Slf4j
public class StudySearchController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RedisUtils redisUtils;

	private final static String APIKEY = "0493706513949334";
	private final static String APISECRET = "NOTvtxnwu9RRTKMquTzjg5N2QnLE8auG";

	// 获取userId
	private final static String GETTOKEN_URL = "https://api.limuzhengxin.com/api/gateway";
	private final static String test = "https://t.limuzhengxin.cn/api/gateway";

	/**
	 * 获取token
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/study")
	public JSONObject getToken(@RequestParam String username, @RequestParam String password,
			@RequestParam String method) throws UnsupportedEncodingException {
		method = "api.education.get";
		String token = redisUtils.get(method + ":" + username);

		if (StringUtils.isBlank(token)) {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("method", method);
			map.add("apiKey", APIKEY);
			map.add("version", "1.2.0");
			map.add("username", username);
			map.add("password", BASE64Utils.getBase64(password));
			String sign = SHA1.createSign(map, false);
			map.add("sign", sign);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);

			JSONObject postForObject = restTemplate.postForObject(GETTOKEN_URL, request, JSONObject.class);

			String code = postForObject.getString("code");
			if ("0010".equals(code)) {
				token = postForObject.getString("token");
				redisUtils.set(method + ":" + username, token, 60 * 60 * 24 * 30); // 过期时间30天
			}
		}

		//查询结果
		JSONObject jsonObject = getInfo(username, "api.common.getResult", "education", token);
		
		//查询状态
//		JSONObject jsonObject = getInfo(username, "api.common.getStatus", "education", token);

		return jsonObject;
	}

	private JSONObject getInfo(String username, String method, String bizType, String token)
			throws UnsupportedEncodingException {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", APIKEY);
		map.add("version", "1.2.0");
		map.add("bizType", "education");
		map.add("token", token);
		String sign = SHA1.createSign(map, false);
		map.add("sign", sign);
		JSONObject postForObject = restTemplate.postForObject(GETTOKEN_URL, map, JSONObject.class);
		return postForObject;
	}
}
