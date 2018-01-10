package com.lichi.increaselimit.third.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "t_email_dic")
@Data
public class EmailDic {
	
	@Id
	private Integer id;
	
	private String name;
	
	private String headerUrl;
	
	private String suffix;
	
	private Integer type;
}
