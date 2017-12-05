package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.BASE64Utils;
import com.lichi.increaselimit.common.utils.LiMuZhengXinUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.third.controller.dto.CreditDto;

import io.swagger.annotations.Api;

/**
 * 征信查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/credit")
@Api(description = "征信查询")
public class CreditController {

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
	public Object getToken(@Valid @RequestBody CreditDto dto,BindingResult result) throws UnsupportedEncodingException {

		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		
		String method = "api.credit.get";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("username", dto.getUsername());
		map.add("password", BASE64Utils.getBase64(dto.getPassword()));
		map.add("middleAuthCode", dto.getMiddleAuthCode());
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);

		JSONObject postForObject = LiMuZhengXinUtils.doPostForToken(restTemplate, map);

		String code = postForObject.getString("code");
		String token = null;
		if ("0010".equals(code)) {
			token = postForObject.getString("token");
		}

		// 查询结果
		JSONObject jsonObject = LiMuZhengXinUtils.getInfo(restTemplate, "api.common.getResult", "credit", token);

		return jsonObject;
	}


}
