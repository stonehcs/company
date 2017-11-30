package com.lichi.increaselimit.user.entity;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 用户信息
 * @author majie
 *
 */
@Data
@Table(name = "t_user")
public class User implements UserDetails,SocialUserDetails{
	
	private static final long serialVersionUID = -368895461220621034L;

	@Id
	private String id;
	
	private String headImg;
	
	@JsonIgnore
	private String password;
	
	private String mobile;
	
	private Integer vipLevel;
	
	private BigDecimal money;
	
	private String username;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return id;
	}
	
}
