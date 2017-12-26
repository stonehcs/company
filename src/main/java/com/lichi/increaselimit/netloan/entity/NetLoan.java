package com.lichi.increaselimit.netloan.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 网贷
 * @author majie
 *
 */
@Data
@Table(name = "t_netloan")
public class NetLoan {
	
	@Id
	private Integer id;
	
	private String title;
	
	private String img;
	
	private String url;
	
	private String description;
	
}
