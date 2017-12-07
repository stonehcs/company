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
public class BillDto {

	@ApiModelProperty("账号")
	private String username;
	
	@ApiModelProperty("密码")
	private String password;
	
	@ApiModelProperty("账单类型，email:邮箱账单")
	@NotBlank(message = "账单类型不能为空")
	private String billType;
	
	
	@ApiModelProperty("账单银行，银行简称代码")
	@NotBlank(message = "账单银行不能为空")
	private String bankCode;
	
	@ApiModelProperty("登陆类型。normal:正常登陆（默认），qr|{邮箱简码}  qr|qq代表qq邮箱扫码登陆")
	private String loginType;
}
