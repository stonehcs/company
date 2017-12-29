package com.lichi.increaselimit.common.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.exception.CardException;
import com.lichi.increaselimit.netloan.exception.CardExceptionVo;
import com.lichi.increaselimit.security.validate.code.ValidateCodeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理
 * 
 * @author majie
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResultVo<?> handler(Exception e) {
		if (e instanceof BusinessException) {
			BusinessException validateException = (BusinessException) e;
			return ResultVoUtil.error(validateException.getCode(), validateException.getMessage());
		} else if (e instanceof CardException) {
			CardException validateException = (CardException) e;
			String bankname = validateException.getIssueBank();
			String last4 = validateException.getLast4digit();
			CardExceptionVo vo = new CardExceptionVo(bankname, last4);
			return ResultVoUtil.error(validateException.getCode(), validateException.getMessage(), vo);
		} else if (e instanceof ValidateCodeException) {
			return ResultVoUtil.error(401, e.getMessage());
		}
		log.error(e.getMessage());
		return ResultVoUtil.error(1, e.getMessage());
	}
}
