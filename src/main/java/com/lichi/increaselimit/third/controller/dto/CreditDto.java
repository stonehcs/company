package com.lichi.increaselimit.third.controller.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditDto {
	
	@ApiModelProperty("账户")
	@NotNull(message="使用账户为空")
	private String username;
	
	@ApiModelProperty("密码")
	@NotNull(message="密码不能为空")
	private String password;
	
	@ApiModelProperty("央行征信身份验证码")
	@NotNull(message="央行征信身份验证码不能为空")
	private String middleAuthCode;
	 
}
