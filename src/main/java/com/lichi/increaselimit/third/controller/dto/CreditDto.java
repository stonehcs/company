package com.lichi.increaselimit.third.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditDto {
	
	@ApiModelProperty("账户")
	@NotBlank(message="使用账户为空")
	private String username;
	
	@ApiModelProperty("密码")
	@NotBlank(message="密码不能为空")
	private String password;
	
	@ApiModelProperty("央行征信身份验证码")
	@NotBlank(message="央行征信身份验证码不能为空")
	private String middleAuthCode;
	 
}
