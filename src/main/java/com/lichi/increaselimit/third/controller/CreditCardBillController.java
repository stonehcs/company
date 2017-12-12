package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.lichi.increaselimit.third.controller.dto.BillDto;

import io.swagger.annotations.Api;

/**
 * 信用卡账单查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/bill")
@Api(description = "信用卡账单查询")
public class CreditCardBillController {

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
	public Object getCreditCardBill(@Valid @RequestBody BillDto billDto, BindingResult result)
			throws UnsupportedEncodingException {

		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}

		String method = "api.bill.get";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("billType", billDto.getBillType());
		map.add("bankCode", billDto.getBankCode());
		if (!StringUtils.isBlank(billDto.getUsername())) {
			map.add("username", billDto.getUsername());
		}
		if (!StringUtils.isBlank(billDto.getPassword())) {
			map.add("password", BASE64Utils.getBase64(billDto.getPassword()));
		}
		if (!StringUtils.isBlank(billDto.getLoginType())) {
			map.add("loginType", billDto.getLoginType());
		}
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);

		JSONObject postForObject = LiMuZhengXinUtils.doPostForToken(restTemplate, map);

		String code = postForObject.getString("code");
		String token = null;
		
		/**
		 *  1.没受理成功直接返回
		 *  2.受理成功以后看状态,是0006直接返回
		 *  3.查询结果
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
