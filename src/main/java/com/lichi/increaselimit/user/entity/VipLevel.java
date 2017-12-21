package com.lichi.increaselimit.user.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户信息
 * @author majie
 *
 */
@Data
public class VipLevel implements Serializable{
	
	private static final long serialVersionUID = 9196847346716343169L;
	
	private Integer id;
	
	private Integer level;
	
	private String levelName;
	
	private Double levelMoney; 
	
}
