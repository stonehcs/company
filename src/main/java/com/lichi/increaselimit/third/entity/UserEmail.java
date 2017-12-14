package com.lichi.increaselimit.third.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	private String password;
}
