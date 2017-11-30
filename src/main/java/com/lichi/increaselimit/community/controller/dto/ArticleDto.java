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
public class ArticleDto implements Serializable {

	private static final long serialVersionUID = -1997630198932216787L;

	@ApiModelProperty(value = "id")
	private Integer id;

	@ApiModelProperty(value = "标题")
	@NotNull(message = "标题不能为空")
	private String title;

	@ApiModelProperty(value = "内容")
	@NotNull(message = "内容不能为空")
	private String content;

	@ApiModelProperty(value = "创建人")
	@NotNull(message = "创建人不能为空")
	private String createUserId;

	@ApiModelProperty(value = "圈子id")
	@NotNull(message = "圈子id不能为空")
	private Integer circleId;
	
	@ApiModelProperty(value = "热门帖子排序")
	private Integer sort;
}
