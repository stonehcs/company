package com.lichi.increaselimit.community.entity;

/**
 * 帖子
 * @author by majie on 2017/11/15.
 */
public class ArticleVo extends Article{

	private static final long serialVersionUID = -1997630198932216787L;

	private String nickname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
