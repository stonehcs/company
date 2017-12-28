package com.lichi.increaselimit.user.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VersionDto {

	@ApiModelProperty("版本号")
	@NotBlank(message="版本号不能为空")
	private String version;
	
	@ApiModelProperty("0安卓  1苹果")
	private Integer type;
	
	@ApiModelProperty("版本信息")
	private String information;
	
	@ApiModelProperty("下载地址")
	private String url;
	
	@ApiModelProperty("是否强制更新 0否 1是")
	private Integer status;
	
}
