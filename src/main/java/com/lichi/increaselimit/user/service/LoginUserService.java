package com.lichi.increaselimit.user.service;

import java.util.List;

import com.lichi.increaselimit.user.entity.LoginUser;

/**
 * 用户login service
 * @author majie
 *
 */
public interface LoginUserService {
	
	/**
	 * 新增一个登陆用户
	 * @param login
	 */
	void addLoginUser(LoginUser login);
	
	/**
	 * 删除一个登陆用户
	 * @param id
	 */
	void deleteLoginUser(String id);
	
	/**
	 * 获取所有的登陆用户
	 * @return
	 */
	List<LoginUser> getAll();
	
}
