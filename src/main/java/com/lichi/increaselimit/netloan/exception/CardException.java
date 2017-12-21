package com.lichi.increaselimit.netloan.exception;

import com.lichi.increaselimit.common.enums.ResultEnum;

/**
 * 业务异常
 * @author majie
 *
 */
public class CardException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private Integer code;
	
	private String last4digit;
	
	private String issueBank;
	
    public CardException(ResultEnum resultEnum,String issueBank,String last4digit) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
        this.last4digit = last4digit;
        this.issueBank = issueBank;
    }

    public CardException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
    
	public String getLast4digit() {
		return last4digit;
	}

	public String getIssueBank() {
		return issueBank;
	}

    
    
}
