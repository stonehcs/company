package com.lichi.increaselimit.user.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_sys_message")
public class SysMessage {

	@Id
	private Integer id;
	
	private String description;
	
	private Integer level;
	
	private String content;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String userId;
}
