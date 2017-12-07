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
public class CourseUpdateDto implements Serializable{
	

	private static final long serialVersionUID = -1897153274744217761L;

	@ApiModelProperty("课程id,必填")
	@NotNull(message = "课程id不能为空")
	private Integer id;
	
	/**
	 * 标题
	 */
	@ApiModelProperty("课程标题")
	private String title;
	/**
	 * 教师id
	 */
	@ApiModelProperty("讲师id")
	private Integer teacherId;
	
	/**
	 * 视频url
	 */
	@ApiModelProperty("视频url")
	private String url;
	
	/**
	 * 观看次数
	 */
	@ApiModelProperty("观看次数,新增不用传入")
	private Integer times;
	
	
	/**
	 * 开课日期
	 */
	@ApiModelProperty("开课时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	/**
	 * 人数
	 */
	@ApiModelProperty("开课人数")
	private Integer person;
	
	/**
	 * 金额.为0就表示免费
	 */
	@ApiModelProperty("课程金额,不传就默认免费")
	private Double money;
	
	/**
	 * 地址
	 */
	@ApiModelProperty("地址")
	private String address;
	
	/**
	 * 是否是精品
	 */
	@ApiModelProperty("是否是精品课程,不传默认否")
	private Integer fine;
	
	
	/**
	 * 1武汉 2成都 3广州 4...
	 */
	@ApiModelProperty("1武汉 2成都 3广州 4...")
	private Integer locationId;
}
