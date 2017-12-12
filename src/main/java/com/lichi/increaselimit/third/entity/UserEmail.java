package com.lichi.increaselimit.third.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 立木邮箱
 * @author majie
 *
 */
@Data
@Table(name = "t_user_email")
public class UserEmail {
	
	@Id
	private String email;
	
	private String userId;
	
	private String password;
}
