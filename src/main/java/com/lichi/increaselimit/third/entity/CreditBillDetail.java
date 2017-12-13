package com.lichi.increaselimit.third.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_credit_bill_detail")
public class CreditBillDetail {
	
	@Id
	private Integer id;
	
	/**
	 * 主表id
	 */
	private String creditBillId;
	/**
	 * 交易日
	 */
	private String transDate;
	/**
	 * 账单周期开始日期
	 */
	private String statementStartDate;
	/**
	 * 账单周期结束日期
	 */
	private String statementEndDate;
	
	/**
	 * 卡号后四位
	 */
	private String last4digit;
	
	/**
	 * 币种
	 */
	private String postCurrency;
	
	/**
	 * 描述
	 */
	private String transDesc;
	
	/**
	 * 交易金额
	 */
	private String postAmt;
	
}
