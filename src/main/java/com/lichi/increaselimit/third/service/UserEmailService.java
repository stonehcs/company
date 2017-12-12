package com.lichi.increaselimit.third.service;

import java.util.List;

import com.lichi.increaselimit.third.entity.UserEmail;

public interface UserEmailService {

	/**
	 * 插入UserEmail
	 * @param userEmail
	 */
	void insert(UserEmail userEmail);

	/**
	 * 通过用户id获取账单
	 * @param userId
	 * @return
	 */
	List<UserEmail> getList(String userId);

	/**
	 * 通过邮箱获取
	 * @param username
	 * @return
	 */
	UserEmail selectByPrimaryKey(String username);

}
