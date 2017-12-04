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
import com.lichi.increaselimit.common.utils.SHA1;
import com.lichi.increaselimit.third.controller.dto.HouseFundDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 公积金controller
 * @author majie
 *
 */
@RestController
@RequestMapping("/housefund")
@Slf4j
@Api(description = "公积金")
public class HouseFundController {

	@Autowired
	private RestTemplate restTemplate;

	private final static String APIKEY = "0493706513949334";
	private final static String APISECRET = "NOTvtxnwu9RRTKMquTzjg5N2QnLE8auG";

	// 获取userId
	private final static String GETTOKEN_URL = "https://api.limuzhengxin.com/api/gateway";
	private final static String TEST_URL = "https://t.limuzhengxin.cn/api/gateway";

	@GetMapping("/area")
	@ApiOperation("获取支持的地区")
	public JSONObject getAreaList() throws UnsupportedEncodingException {
		String method = "api.housefund.getareas";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", APIKEY);
		map.add("version", "1.2.0");
		String sign = SHA1.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);

		JSONObject postForObject = restTemplate.postForObject(TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	@GetMapping("/area/{areaCode}")
	@ApiOperation("公积金地区登陆元素查")
	public JSONObject getAreaList(@PathVariable String areaCode) throws UnsupportedEncodingException {
		String method = "api.housefund.getloginelements";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", APIKEY);
		map.add("version", "1.2.0");
		map.add("areaCode", areaCode);
		String sign = SHA1.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		
		JSONObject postForObject = restTemplate.postForObject(TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	@PostMapping
	@ApiOperation("公积金查询")
	public JSONObject getResult(@RequestBody HouseFundDto dto) throws UnsupportedEncodingException {
		String method = "api.housefund.get";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", APIKEY);
		map.add("version", "1.2.0");
		map.add("username", dto.getUsername());
		map.add("password", dto.getPassword());
		map.add("area", dto.getArea());
		
		String sign = SHA1.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		
		JSONObject postForObject = restTemplate.postForObject(TEST_URL, request, JSONObject.class);
		
		return postForObject;
	}
	
	
	
}
