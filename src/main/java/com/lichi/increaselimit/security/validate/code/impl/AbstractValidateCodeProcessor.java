package com.lichi.increaselimit.security.validate.code.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.utils.StringUtil;
import com.lichi.increaselimit.security.properties.SecurityConstants;
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.security.validate.code.ValidateCodeException;
import com.lichi.increaselimit.security.validate.code.ValidateCodeGenerator;
import com.lichi.increaselimit.security.validate.code.ValidateCodeProcessor;
import com.lichi.increaselimit.security.validate.code.ValidateCodeType;
import com.lichi.increaselimit.security.validate.code.repository.ValidateCodeRepository;

/**
 * @author majie
 *
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	/**
	 * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
	 */
	@Autowired
	private Map<String, ValidateCodeGenerator> validateCodeGenerators;
	
	@Autowired
	private ValidateCodeRepository validateCodeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeProcessor#create(org.
	 * springframework.web.context.request.ServletWebRequest)
	 */
	@Override
	public void create(ServletWebRequest request) throws Exception {
		C validateCode = generate(request);
		save(request, validateCode);
		send(request, validateCode);
	}

	/**
	 * 生成校验码
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private C generate(ServletWebRequest request) {
		String type = getValidateCodeType(request).toString().toLowerCase();
		String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
		ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
		if (validateCodeGenerator == null) {
			throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
		}
		return (C) validateCodeGenerator.generate(request);
	}

	/**
	 * 保存校验码
	 * 
	 * @param request
	 * @param validateCode
	 */
	private void save(ServletWebRequest request, C validateCode) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request,code,getValidateCodeType(request));
	}

	/**
	 * 发送校验码，由子类实现
	 * 
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

	/**
	 * 根据请求的url获取校验码的类型
	 * 
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
		return ValidateCodeType.valueOf(type.toUpperCase());
	}

	/**
	 * 先验证手机号
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void validate(ServletWebRequest request) {

		ValidateCodeType codeType = getValidateCodeType(request);
		
		if(codeType.equals(ValidateCodeType.SMS)) {
			boolean isMobile = StringUtil.ValidateMobile(request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE));
			if(!isMobile) {
				throw new ValidateCodeException(ResultEnum.MOBILE_ERROR.getMessage());
			}
		}
		C codeInRedis = (C) validateCodeRepository.get(request, codeType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
					codeType.getParamNameOnValidate());
		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("获取验证码的值失败");
		}

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException(codeType + "验证码的值不能为空");
		}

		if (codeInRedis == null) {
			throw new ValidateCodeException(codeType + "验证码不存在");
		}

		if (codeInRedis.isExpried()) {
			validateCodeRepository.remove(request, codeType);
			throw new ValidateCodeException(codeType + "验证码已过期");
		}

		if (!StringUtils.equals(codeInRedis.getCode(), codeInRequest)) {
			throw new ValidateCodeException(codeType + "验证码不匹配");
		}
		
		/**
		 * 登陆成功移除验证码
		 */
		validateCodeRepository.remove(request, codeType);
	}

}
