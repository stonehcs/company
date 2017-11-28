package com.lichi.increaselimit.security.validate.code.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.security.properties.SecurityConstants;
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.security.validate.code.ValidateCodeException;
import com.lichi.increaselimit.security.validate.code.ValidateCodeType;
import com.lichi.increaselimit.security.validate.code.repository.ValidateCodeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 验证码的存放使用redis
 * 
 * @author majie
 * @param <T>
 *
 */
@Component
@Slf4j
public class RedisValidateCodeRepository<T> implements ValidateCodeRepository {
	
	@Autowired
	private RedisUtils redisUtils;

	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
		redisUtils.set(buildKey(request, type), JSONObject.toJSONString(code), 60);
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
		Object value = redisUtils.get(buildKey(request, type));
		if (value == null) {
			return null;
		}
		return JSONObject.parseObject(value + "", ValidateCode.class);
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType type) {
		redisUtils.del(buildKey(request, type));
	}

	/**
	 * 短信登陆就保存手机号
	 * 
	 * @param request
	 * @param type
	 * @return
	 */
	private String buildKey(ServletWebRequest request, ValidateCodeType type) {
		String code = "";
		if("sms".equals(type.toString().toLowerCase())) {
			String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
			try {
				code = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
			} catch (ServletRequestBindingException e) {
				log.error("获取手机号码{}的验证码失败",code);
			}
		}else {
			code = request.getHeader("deviceId");
			if (StringUtils.isBlank(code)) {
				throw new ValidateCodeException("请在请求头中携带deviceId参数");
			}
		}
		return "code:" + type.toString().toLowerCase() + ":" + code;
	}
}
