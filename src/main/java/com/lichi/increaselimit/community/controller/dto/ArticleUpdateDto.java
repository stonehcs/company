package com.lichi.increaselimit.community.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Article新增的时候数据传输对象
 * 
 * @author majie
 *
 */
@Data
@ApiModel("帖子")
public class ArticleUpdateDto implements Serializable {

	private static final long serialVersionUID = -1997630198932216787L;

	@ApiModelProperty(value = "id")
	@NotNull(message = "帖子id,必填")
	private Integer id;

	@ApiModelProperty(value = "标题")
	private String title;

	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "热门帖子排序")
	private Integer sort;
}
