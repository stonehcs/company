package com.lichi.increaselimit.user.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户详情
 * @author majie
 *
 */
@Data
@Table(name = "t_user_details")
public class UserDetails implements Serializable{

	private static final long serialVersionUID = 954468051256924402L;
	
	@Id
	private Integer id;
	
	private String userId;
	
	

}
