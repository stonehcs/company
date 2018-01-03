package com.lichi.increaselimit.user.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "mobile")
@Data
public class Mobile {
	
	@Id
	private Integer id;
	
	private String mobile;
}
