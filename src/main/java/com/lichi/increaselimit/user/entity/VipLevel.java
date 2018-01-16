package com.lichi.increaselimit.user.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户信息
 * @author majie
 *
 */
@Data
@Table(name = "t_vip_level")
public class VipLevel implements Serializable{
	
	private static final long serialVersionUID = 9196847346716343169L;
	
	@Id
	private Integer id;
	
	private Integer level;
	
	private String levelName;
	
	private Double levelMoney; 
	
	private Double percent;
}
