package com.lichi.increaselimit.security.validate.code.sms;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.security.properties.SecurityProperties;
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.security.validate.code.ValidateCodeGenerator;
import com.lichi.increaselimit.security.validate.code.ValidateCodeType;
import com.lichi.increaselimit.security.validate.code.repository.impl.RedisValidateCodeRepository;

/**
 * 生成短信验证码
 * @author majie
 *
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private RedisValidateCodeRepository<ValidateCode> redisValidateCodeRepository;
	
	/**
	 * 先校验验证码是否存在，如果存在一分钟后再发
	 */
	@Override
	public ValidateCode generate(ServletWebRequest request) {
		
		ValidateCode validateCode = redisValidateCodeRepository.get(request, ValidateCodeType.SMS);
		if(validateCode != null) {
			throw new BusinessException(ResultEnum.CODE_EXIST);
		}
		String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
		return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
	
	

}
