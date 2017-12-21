package com.lichi.increaselimit.netloan.exception;

/**
 * 信用卡对应信息
 * @author majie
 *
 */
public class CardExceptionVo{

	private Integer code;

	private String last4digit;
	
	private String issueBank;
	
    public CardExceptionVo(String issueBank,String last4digit) {

        this.last4digit = last4digit;
        this.issueBank = issueBank;
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
