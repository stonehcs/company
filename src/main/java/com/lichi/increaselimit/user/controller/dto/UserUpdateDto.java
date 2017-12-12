package com.lichi.increaselimit.user.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserUpdateDto {

	@ApiModelProperty("用户id,必填")
	@NotBlank(message = "用户id不能为空")
	private String id;
	
	@ApiModelProperty("头像")
	private String headImg;
	
	@ApiModelProperty("密码")
	@JsonIgnore
	private String password;
	
	@ApiModelProperty("手机号")
	private String mobile;
	
	@ApiModelProperty("等级")
	private Integer vipLevel;
	
	@ApiModelProperty("佣金")
	private Double money;
	
	@ApiModelProperty("昵称")
	private String nickname;

	@ApiModelProperty("邀请人数")
	private Integer rank;
	
	@ApiModelProperty("积分")
	private Integer points;
	
	@ApiModelProperty("验证码")
	private String code;
}
