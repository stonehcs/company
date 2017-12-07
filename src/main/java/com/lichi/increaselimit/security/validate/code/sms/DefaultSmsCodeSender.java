package com.lichi.increaselimit.security.validate.code.sms;

import org.springframework.scheduling.annotation.Async;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送短信
 * @author majie
 *
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

	@Override
	@Async
	public void send(String mobile, String code) {
		log.info("向手机"+mobile+"发送短信验证码"+code);
	}

}
