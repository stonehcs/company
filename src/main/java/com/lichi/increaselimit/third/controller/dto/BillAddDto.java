package com.lichi.increaselimit.third.controller.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class BillAddDto {

	@NotBlank(message = "账单月不能为空")
	private String date;
	
	@NotBlank(message = "账单金额不能为空")
	private String money;
	
	@NotNull(message = "请选择信用卡id")
	private Integer id;
}
