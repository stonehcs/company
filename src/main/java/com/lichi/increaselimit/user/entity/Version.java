package com.lichi.increaselimit.user.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;

/**
 * 版本信息
 * @author majie
 *
 */
@Data
@Table(name = "t_version")
public class Version {

	@Id
	private Integer id;
	
	private String version;
	
	private Integer type;
	
	private String information;
	
	private String url;
	
	private Integer status;
	
	@OrderBy("desc")
	private Date createTime;
	
	private Date updateTime;
	
	
}
