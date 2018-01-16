package com.lichi.increaselimit.course.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_commission")
public class Commission {

	@Id
	private Integer id;
	
	private String userId;
	
	private String orderId;
	
	private Double money;
	
	private Date createTime;
	
	private Date updateTime;
}
