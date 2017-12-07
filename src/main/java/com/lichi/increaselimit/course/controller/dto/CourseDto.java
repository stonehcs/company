package com.lichi.increaselimit.course.controller.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程实体
 * @author majie
 *
 */
@Data
@ApiModel("课程实体")
public class CourseDto implements Serializable{
	

	private static final long serialVersionUID = 1469611795884966933L;

	@ApiModelProperty("课程标题")
	@NotNull(message = "课程标题不能为空")
	private String title;
	
	@ApiModelProperty("讲师id")
	@NotNull(message = "讲师id不能为空")
	private Integer teacherId;
	
	@ApiModelProperty("视频url")
	private String url;
	
	@ApiModelProperty("观看次数,新增不用传入")
	private Integer times;
	
	@ApiModelProperty("开课时间")
	@NotNull(message = "开课时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@ApiModelProperty("开课人数")
	@NotNull(message = "开课人数不能为空")
	private Integer person;
	
	/**
	 * 金额.为0就表示免费
	 */
	@ApiModelProperty("课程金额,不传就默认免费")
	private Double money;
	
	@ApiModelProperty("地址")
	@NotNull(message = "地址不能为空")
	private String address;
	
	@ApiModelProperty("是否是精品课程,不传默认否")
	private Integer fine;
	
	@ApiModelProperty("1武汉 2成都 3广州 4...")
	private Integer locationId;
}
