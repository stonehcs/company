package com.lichi.increaselimit.course.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_video")
public class Video implements Serializable{
	
	private static final long serialVersionUID = -5010017391655225352L;
	
	@Id
	private Integer id;
	
	private Double price;
	
	private Integer level;
	
	private String description;
	
	private String url;
}
