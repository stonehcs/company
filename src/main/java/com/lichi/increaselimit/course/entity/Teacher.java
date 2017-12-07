package com.lichi.increaselimit.course.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 讲师
 * @author majie
 *
 */
@Data
@Table(name = "t_teacher")
public class Teacher implements Serializable{

	private static final long serialVersionUID = 3308075062590670824L;
	
	@Id
	private Integer id;
	
	private String teachername;
	
	private String introduce;
	
	private String imgUrl;
	
	private Date updateTime;
	private Date createTime;
}
