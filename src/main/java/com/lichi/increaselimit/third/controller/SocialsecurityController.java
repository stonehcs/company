package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
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
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.third.controller.dto.ThirdUserInfoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 社保
 */
@RestController
@Api(description="社保查询")
@RequestMapping("/socialsecurity")
@Slf4j
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
	public Object getResult(@Valid @RequestBody ThirdUserInfoDto dto,BindingResult result) throws UnsupportedEncodingException {
       if(result.hasErrors()){
            String errors = result.getFieldError().getDefaultMessage();
            return ResultVoUtil.error(1,errors);
        }
		String method = "api.socialsecurity.get";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("username", dto.getUsername());
		map.add("password", BASE64Utils.getBase64(dto.getPassword()));
		map.add("area", dto.getArea());
		
		if(!StringUtils.isBlank(dto.getOtherInfo())) {
			map.add("otherInfo", dto.getOtherInfo());
		}
		if(!StringUtils.isBlank(dto.getRealName())) {
			map.add("realName", dto.getRealName());
		}
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		
		JSONObject postForObject = restTemplate.postForObject(LiMuZhengXinUtils.TEST_URL, request, JSONObject.class);
		String code = postForObject.getString("code");
		
		String token = null; 
		if("0010".equals(code)) {
			token = postForObject.getString("token");
			log.info("社保查询token:{}",token);
			// 查询结果
			postForObject = LiMuZhengXinUtils.getInfo(restTemplate, "api.common.getResult", "socialsecurity", postForObject.getString("token"));
		}
		return postForObject;
	}
}
