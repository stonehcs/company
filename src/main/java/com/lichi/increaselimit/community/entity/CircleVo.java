package com.lichi.increaselimit.community.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 圈子vo
 * 
 * @author by majie on 2017/11/16.
 */
@Data
public class CircleVo implements Serializable {

	private static final long serialVersionUID = 7705611255014101585L;

	private Integer id;

	private String name;

	private String nickname;
	
	private String createUserId;

	private Date createTime;

	private Date updateTime;

	private String url;
	
	private String introduce;

	private Integer count = 0;

	private Integer sort1;

	private Integer sort2;
}
