package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.BASE64Utils;
import com.lichi.increaselimit.common.utils.LiMuZhengXinUtils;
import com.lichi.increaselimit.third.controller.dto.UserInfoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 社保
 */
@RestController
@Api(description="社保查询")
@RequestMapping("/socialsecurity")
public class SocialsecurityController {
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/area")
	@ApiOperation("获取支持的地区")
	public JSONObject getAreaList() throws UnsupportedEncodingException {
		String method = "api.socialsecurity.getareas";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);

		JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	@GetMapping("/area/{areaCode}")
	@ApiOperation("社保地区登陆元素查")
	public JSONObject getAreaList(@PathVariable String areaCode) throws UnsupportedEncodingException {
		String method = "api.socialsecurity.getloginelements";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("areaCode", areaCode);
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		
		JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	@PostMapping
	@ApiOperation("社保查询")
	public JSONObject getResult(@RequestBody UserInfoDto dto) throws UnsupportedEncodingException {
		String method = "api.socialsecurity.get";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("username", dto.getUsername());
		map.add("password", BASE64Utils.getBase64(dto.getPassword()));
		map.add("area", dto.getArea());
		
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		
		JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
}
