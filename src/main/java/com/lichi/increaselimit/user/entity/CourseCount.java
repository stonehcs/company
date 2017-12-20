package com.lichi.increaselimit.user.entity;

public class CourseCount {
	
	private Integer pay;
	
	private Integer signUp;

	public Integer getPay() {
		return pay;
	}

	public void setPay(Integer pay) {
		this.pay = pay;
	}

	public Integer getSignUp() {
		return signUp;
	}

	public void setSignUp(Integer signUp) {
		this.signUp = signUp;
	}

	public CourseCount(Integer pay, Integer signUp) {
		super();
		this.pay = pay;
		this.signUp = signUp;
	}
	
	
}
