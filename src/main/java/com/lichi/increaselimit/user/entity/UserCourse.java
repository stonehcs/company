package com.lichi.increaselimit.user.entity;

import javax.persistence.Table;

import lombok.Data;

/**
 * 用户课程
 * 
 * @author Rodger.Young
 *
 */
@Data
@Table(name="t_user_course")
public class UserCourse {
	private String user_id;
	private Integer course_id;
	private Integer status;
}
