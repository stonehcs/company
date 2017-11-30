package com.lichi.increaselimit.netloan.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 诊断结果
 * @author majie
 *
 */
@Data
@Table(name = "t_diagnosis_result")
public class DiagnosisResult implements Serializable{
	
	private static final long serialVersionUID = 3333860422843700302L;

	@Id
	private Integer id;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 类型  1 固定 2百分比
	 */
	private Integer type;
	
	
	/**
	 * 银行名字
	 */
	private String bankname;
	
	/**
	 * 消费名称
	 */
	private String name;
	
	/**
	 * 消费金额
	 */
	private Double consumeMoney;
	
	/**
	 * 应该消费金额
	 */
	private Double money;
	
	
}
