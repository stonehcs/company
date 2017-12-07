package com.lichi.increaselimit.course.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师
 * @author majie
 *
 */
@Data
public class TeacherUpdateDto implements Serializable{

	
	private static final long serialVersionUID = 8204456060633021678L;

	@ApiModelProperty("讲师id,必填")
	@NotNull(message = "讲师id不能为空")
	private Integer id;
	
	@ApiModelProperty("老师姓名")
	private String teachername;
	
	@ApiModelProperty("老师简介")
	private String introduce;
	
	@ApiModelProperty("讲师头像")
	private String imgUrl;
	
}
