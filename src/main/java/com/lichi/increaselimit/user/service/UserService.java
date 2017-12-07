package com.lichi.increaselimit.user.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.user.entity.SocialUserInfo;
import com.lichi.increaselimit.user.entity.User;

/**
 * 用户service
 * @author majie
 *
 */
public interface UserService {
	
	/**
	 * 通过手机号获取用户信息
	 * @param username
	 * @return
	 */
	User loadUserInfoByMobile(String mobile);
	/**
	 * 通过手机号获取用户信息
	 * @param username
	 * @return
	 */
	User loadUserInfoByUsername(String username);

	/**
	 * 通过用户id获取用户信息
	 * @param userId
	 * @return
	 */
	User loadUserInfoByUserId(String userId);
	
	/**
	 * 插入第三方用户信息
	 * @param socialUserInfo
	 * @return 
	 */
	User insertSocialUser(SocialUserInfo socialUserInfo);
	
	/**
	 * 插入手机用户
	 * @param socialUserInfo
	 * @return 
	 */
	User insertMobileUser(String mobile);
	
	/**
	 * 分页查询排行榜
	 * @param page
	 * @param sizeInteger
	 * @return
	 */
	PageInfo<User> selectBank(Integer page, Integer sizeInteger);
	
	/**
	 * 查询课程信息
	 * @param id
	 * @param page
	 * @param size
	 * @return
	 */
	PageInfo<Course> selectCourse(Integer page, Integer size, String id, Integer status);
	
	/**
	 * 更新用户信息
	 * @param user
	 */
	void updateUserInfo(User user);
}
