package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.BASE64Utils;
import com.lichi.increaselimit.common.utils.LiMuZhengXinUtils;

import io.swagger.annotations.Api;

/**
 * 立木征信 学历查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/study")
@Api(description="学历查询")
public class StudySearchController {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 获取token
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping
	public JSONObject getToken(@RequestParam String username, @RequestParam String password,
			@RequestParam String method) throws UnsupportedEncodingException {
		method = "api.education.get";

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("method", method);
			map.add("apiKey", LiMuZhengXinUtils.APIKEY);
			map.add("version", "1.2.0");
			map.add("username", username);
			map.add("password", BASE64Utils.getBase64(password));
			String sign = LiMuZhengXinUtils.createSign(map, false);
			map.add("sign", sign);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);

			JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, request, JSONObject.class);

			String code = postForObject.getString("code");
			String token = null;
			if ("0010".equals(code)) {
				token = postForObject.getString("token");
		}

		//查询结果
		JSONObject jsonObject = getInfo("api.common.getResult", "education", token);
		
		//查询状态
//		JSONObject jsonObject = getInfo(username, "api.common.getStatus", "education", token);

		return jsonObject;
	}

	private JSONObject getInfo(String method, String bizType, String token)
			throws UnsupportedEncodingException {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("bizType", "education");
		map.add("token", token);
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);
		JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, map, JSONObject.class);
		return postForObject;
	}
}
