package com.lichi.increaselimit.third.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 信用卡账单
 * @author majie
 *
 */
@Getter
@Setter
@ApiModel("信用卡账单")
public class UserEmailDto {

	@ApiModelProperty("账号")
	@NotBlank(message = "账号不能为空")
	private String username;
	
	@ApiModelProperty("用户id")
	@NotBlank(message = "用户id不能为空")
	private String userId;
	
	@ApiModelProperty("密码")
	@NotBlank(message = "密码不能为空")
	private String password;
	
}
