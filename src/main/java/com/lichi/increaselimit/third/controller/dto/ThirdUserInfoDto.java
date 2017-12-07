package com.lichi.increaselimit.third.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询参数
 * @author majie
 *
 */
@Getter
@Setter
public class ThirdUserInfoDto {
	
	@NotBlank(message = "账号不能为空")
	private String username;
	@NotBlank(message = "密码不能为空")
	private String password;
	@NotBlank(message = "地区不能为空")
	private String area;
	
	private String realName;
	
	private String otherInfo;
	
}
