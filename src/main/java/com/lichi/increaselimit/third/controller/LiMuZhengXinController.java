package com.lichi.increaselimit.third.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.BASE64Utils;
import com.lichi.increaselimit.common.utils.LiMuZhengXinUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.third.controller.dto.BillDto;
import com.lichi.increaselimit.third.entity.UserEmail;
import com.lichi.increaselimit.third.service.UserEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 信用卡账单查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/bills")
@Api(description = "信用卡账单查询")
public class LiMuZhengXinController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserEmailService userEmailService;

	@ApiOperation("登陆获取信用卡账单")
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public Object getCreditCardBill(@Valid @RequestBody BillDto billDto, BindingResult result) {

		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		String username = billDto.getUsername();
		String password = billDto.getPassword();

		try {
//			/**
//			 * 保存账户密码
//			 */
//			UserEmail userEmail = new UserEmail();
//			BeanUtils.copyProperties(billDto, userEmail);
//			userEmail.setEmail(username);
//			UserEmail user =  userEmailService.selectByPrimaryKey(username);
//			if(user == null) {
//				userEmailService.insert(userEmail);
//			}
			return getByUsername(username, password);

		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(ResultEnum.NO_RESPONSE);
		}
	}

	@ApiOperation("直接获取信用卡账单")
	@GetMapping("/{userId}")
	public Object getCreditCardBill(@PathVariable String userId) {
		
		List<JSONObject> result = new ArrayList<>(); 
		List<UserEmail> list = userEmailService.getList(userId);

		for (UserEmail userEmail : list) {
			try {
				JSONObject json = getByUsername(userEmail.getEmail(), userEmail.getPassword());
				result.add(json);
			} catch (UnsupportedEncodingException e) {
				throw new BusinessException(ResultEnum.NO_RESPONSE);
			}

		}
		return ResultVoUtil.success(result);
	}
	
	@ApiOperation("获取邮箱列表")
	@GetMapping("/list/{userId}")
	public Object getEmailList(@PathVariable String userId) {
		
		List<UserEmail> list = userEmailService.getList(userId);
		
		return ResultVoUtil.success(list);
	}

	private JSONObject getByUsername(String username, String password) throws UnsupportedEncodingException {
		String method = "api.bill.get";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("method", method);
		map.add("apiKey", LiMuZhengXinUtils.APIKEY);
		map.add("version", "1.2.0");
		map.add("billType", "email");
		map.add("bankCode", "all");
		map.add("username", username);
		map.add("password", BASE64Utils.getBase64(password));
		// if (!StringUtils.isBlank(billDto.getLoginType())) {
		// map.add("loginType", billDto.getLoginType());
		// }
		String sign = LiMuZhengXinUtils.createSign(map, false);
		map.add("sign", sign);

		JSONObject postForObject = LiMuZhengXinUtils.doPostForToken(restTemplate, map);

		String code = postForObject.getString("code");
		String token = null;

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
		if ("0000".equals(postForObject.getString("code"))) {
			postForObject = LiMuZhengXinUtils.getInfo(restTemplate, "getResult", "bill", token);
		}
		
		return postForObject;
	}
}
