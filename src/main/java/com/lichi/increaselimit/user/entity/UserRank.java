package com.lichi.increaselimit.user.entity;

import lombok.Data;

@Data
public class UserRank {
	
	private String id;
	
	private Integer rownum;
	
	private Integer points;

	private Integer rank;
	
	private Integer diff;
}
