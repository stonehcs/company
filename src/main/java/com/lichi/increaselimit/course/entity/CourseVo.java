package com.lichi.increaselimit.course.entity;


/**
 * Course查询扩展类
 * @author majie
 *
 */
public class CourseVo extends Course{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6203838632198702455L;

	private String teachername;
	
	private String introduce;
	
	private String imgUrl;
	
	private Integer status;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTeachername() {
		return teachername;
	}

	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
