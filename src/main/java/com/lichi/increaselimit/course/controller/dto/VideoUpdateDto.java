package com.lichi.increaselimit.course.controller.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoUpdateDto {

	@ApiModelProperty("视频id")
	@NotNull(message = "视频id不能为空")
	private Integer id;
	
	@ApiModelProperty("价格,如果免费,默认为0")
	private Double price = 0d;
	
	@ApiModelProperty("视频等级")
	private Integer level;
	
	@ApiModelProperty("视频描述")
	private String description;
	
	@ApiModelProperty("视频url")
	private String url;
	
	@ApiModelProperty("视频封面")
	private String videoUrl;
}
