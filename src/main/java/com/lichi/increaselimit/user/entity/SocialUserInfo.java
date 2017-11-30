package com.lichi.increaselimit.user.entity;

import java.io.Serializable;

import javax.persistence.Table;

import lombok.Data;

/**
 * 第三方用户登陆表
 * 
 * @author majie
 *
 */
@Data
@Table(name = "t_UserConnection")
public class SocialUserInfo implements Serializable {

	private static final long serialVersionUID = 4311034992993297633L;

	private String userId;
	private String providerId;
	private String providerUserId;
	private Integer rank;
	private String displayName;
	private String profileUrl;
	private String imageUrl;
	private String accessToken;
	private String secret;
	private String refreshToken;
	private Long expireTime;

}
