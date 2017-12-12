package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.LiMuZhengXinUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

/**
 * 短信验证码接口
 * 
 * @author majie
 *
 */
@RestController
@Api(description = "立木验证码")
public class CodeValidateController {

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
	@GetMapping("/codevalidate")
	public Object getToken(@ApiParam(value = "token", required = true) @RequestParam(required = true) String token,
			@ApiParam(value = "短信验证码", required = true) @RequestParam(required = true) String input)
			throws UnsupportedEncodingException {

		String method = "api.common.input";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("token", token);
		map.add("input", input);
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);

		JSONObject postForObject = LiMuZhengXinUtils.doPostForToken(restTemplate, map);
		String code = postForObject.getString("code");

		/**
		 * 1.没受理成功直接返回 2.受理成功以后看状态,是0006直接返回 3.查询结果
		 */
		if (!"0010".equals(code)) {
			return postForObject;
		}
		token = postForObject.getString("token");

		postForObject = LiMuZhengXinUtils.getInfo(restTemplate, "getStatus", "bill", token);
		if ("0006".equals(postForObject.getString("code"))) {
			return postForObject;
		}

		postForObject = LiMuZhengXinUtils.getInfo(restTemplate, "getResult", "bill", token);
		return postForObject;

	}
}
