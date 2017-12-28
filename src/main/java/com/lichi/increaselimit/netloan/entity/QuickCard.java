package com.lichi.increaselimit.netloan.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 快卡
 * @author majie
 *
 */
@Data
@Table(name = "t_quick_card")
public class QuickCard {
	
	@Id
	private Integer id;
	
	private String title;
	
	private String img;
	
	private String url;
	
	private String description;
	
	private Integer type;
	
}
