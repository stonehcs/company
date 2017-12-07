package com.lichi.increaselimit.third.controller.dto;

import javax.validation.constraints.NotNull;

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
	
	@NotNull(message = "账号不能为空")
	private String username;
	@NotNull(message = "密码不能为空")
	private String password;
	@NotNull(message = "地区不能为空")
	private String area;
	
	private String realName;
	
	private String otherInfo;
	
}
