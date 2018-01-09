package com.lichi.increaselimit.third.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Table(name = "t_credit")
public class Credit {
	
	@Id
	private Integer id;
	
	private String last4digit;
	
	private String issueBank;
	
	private String holderName;
	
	/**
	 * 还款日
	 */
	private String paymentDueDate;
	
	/**
	 * 账单日
	 */
	private String statementDate;
	
	/**
	 * 总额度
	 */
	private String creditAmt;
	
	/**
	 * 应还金额
	 */
	private String balanceRmb;

	private String userId;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String email;
	
	private Integer freeDay;
	
	@Transient
	private Integer payDay = 0;
	
	@Transient
	private Integer billDay = 0;
}
