package com.lichi.increaselimit.course.controller.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author majie
 *
 */
@Getter
@Setter
public class SignUpDto {
	

	@ApiModelProperty("课程id")
	@NotNull(message = "课程id不能为空")
	private Integer id;
	
	@ApiModelProperty("用户id")
	private String userId;
	
	@ApiModelProperty("姓名")
	@NotBlank(message = "姓名不能为空")
	private String nickname;
	
	@ApiModelProperty("手机号")
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	
	@ApiModelProperty("备注")
	private String remark;
	
	@ApiModelProperty("验证码")
	private String code;
	
	
	
	
	
}
