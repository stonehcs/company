package com.lichi.increaselimit.third.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 信用卡账单
 * @author majie
 *
 */
@Data
@Table(name = "t_credit_bill")
public class CreditBill {
	
	@Id
	private String id;
	
	private String userId;
	
	/**
	 * 应还金额
	 */
	private String balanceRmb;
	
	/**
	 * 发卡行
	 */
	private String issueBank;
	
	/**
	 * 持卡人姓名
	 */
	private String holderName;
	
	/**
	 * 卡号后四位
	 */
	private String last4digit;
	
	/**
	 * 还款日
	 */
	private String paymentDueDate;
	
	/**
	 * 免息期(免息期等于还款日-账单周期开始日期)
	 */
	private Integer freeDay;
	
	/**
	 * 出账日
	 */
	private String statementEndDate;
	
	/**
	 * 账单周期开始日期
	 */
	private String statementStartDate;
	
	/**
	 * 最低应还金额
	 */
	private String minPaymentRmb;
	
	/**
	 * 积分
	 */
	private String availablePoints;
	
	/**
	 * 总额度
	 */
	private String creditAmt;
	
	/**
	 * 取现额度
	 */
	private String cashLimitAmt;
}
