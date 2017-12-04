package com.lichi.increaselimit.user.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户登陆表
 * @author majie
 *
 */
@Data
@Table(name = "t_login_user")
public class LoginUser {
	
	@Id
	private String id;
	
	/**
	 * 登陆时间
	 */
	private Date loginTime;
}
