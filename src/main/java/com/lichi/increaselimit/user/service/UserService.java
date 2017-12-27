package com.lichi.increaselimit.user.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.user.entity.CourseCount;
import com.lichi.increaselimit.user.entity.SocialUserInfo;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserRank;
import com.lichi.increaselimit.user.entity.VipLevel;

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
	 * 插入用户
	 * @param socialUserInfo
	 * @return 
	 */
	User insert(User user);
	
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
	 * @param status 
	 * @return
	 */
	PageInfo<CourseVo> selectCourse(Integer page, Integer size, String id, Integer status);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return 
	 */
	User updateUserInfo(User user);
	
	/**
	 * 获取当前用户排名
	 * @param id
	 * @return
	 */
	UserRank getUserRank(String id);
	
	/**
	 * 获取我的课程
	 * @param id
	 * @return
	 */
	CourseCount getMyCourse(String id);
	
	/**
	 * 通过levelId等级获取level信息
	 * @param level
	 * @return
	 */
	VipLevel getLevel(Integer level);
	
	/**
	 * 注册环信并更新pid用户的邀请次数
	 * @param pid
	 * @param userId 
	 */
	void registerHuanxinAndUpdatePid(String pid, String userId);
}
