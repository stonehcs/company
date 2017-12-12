package com.lichi.increaselimit.course.controller.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoDto {

	@ApiModelProperty("价格,如果免费,默认为0")
	@NotNull(message = "价格不能为空")
	private Double price = 0d;
	
	@ApiModelProperty("视频等级")
	@NotNull(message = "视频等级不能为空")
	private Integer level;
	
	@ApiModelProperty("视频描述")
	@NotBlank(message = "视频描述不能为空")
	private String description;
	
	@ApiModelProperty("视频url")
	@NotBlank(message = "视频url不能为空")
	private String url;
}
