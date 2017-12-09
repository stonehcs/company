package com.lichi.increaselimit.course.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 课程实体
 * @author majie
 *
 */
@Table(name = "t_course")
@Data
public class Course implements Serializable{
	
	private static final long serialVersionUID = -633059250377663540L;

	@Id
	private Integer id;
	
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 教师id
	 */
	private Integer teacherId;
	
	/**
	 * 视频url
	 */
	private String url;
	
	/**
	 * 观看次数
	 */
	private Integer times;
	
	
	/**
	 * 开课日期
	 */
	private Date startTime;
	
	/**
	 * 结课日期
	 */
	private Date endTime;
	
	/**
	 * 人数
	 */
	private Integer person;
	
	/**
	 * 金额.为0就表示免费
	 */
	private Double money;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 是否是精品
	 */
	private Integer fine;
	
	/**
	 * 1武汉 2成都 3广州 4...
	 */
	private Integer locationId;
	
	private Date updateTime;
	private Date createTime;
}
