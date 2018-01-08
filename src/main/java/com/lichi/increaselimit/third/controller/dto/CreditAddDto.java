package com.lichi.increaselimit.third.controller.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreditAddDto {
	
	@ApiModelProperty
	@NotBlank(message = "银行卡后四位不能为空")
	@Length(min=4,max=4,message="银行卡后四位只能是四位")
	private String last4digit;
	
	@NotBlank(message = "发卡行不能为空")
	private String issueBank;
	
	@NotBlank(message = "持卡人名称不能为空")
	private String holderName;
	
	/**
	 * 还款日
	 */
	@NotBlank(message = "还款日不能为空")
	private String paymentDueDate;
	
	/**
	 * 账单日
	 */
	@NotBlank(message = "账单日不能为空")
	private String statementDate;
	
	/**
	 * 总额度
	 */
	@NotBlank(message = "总额度不能为空")
	private String creditAmt;
	
	/**
	 * 应还金额
	 */
	@NotBlank(message = "应还金额不能为空")
	private String balanceRmb;

	
	
	
}
