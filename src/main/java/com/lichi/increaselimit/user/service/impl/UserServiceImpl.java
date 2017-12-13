package com.lichi.increaselimit.user.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.HuanXinUtils;
import com.lichi.increaselimit.common.utils.IdUtils;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.user.dao.SocialUserMapper;
import com.lichi.increaselimit.user.dao.UserMapper;
import com.lichi.increaselimit.user.entity.SocialUserInfo;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SocialUserMapper socialUserMapper;
	
	@Autowired
	private RestTemplate restTemplate;
 
	@Override
	public User loadUserInfoByMobile(String mobile) {
		User user = userMapper.loadUserInfoByMobile(mobile);
		return user;
	}

	@Override
	public User loadUserInfoByUserId(String userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		return user;
	}

	/**
	 * 还要注册环信用户
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insertSocialUser(SocialUserInfo socialUserInfo) {
		User user = new User();
		String userId = IdUtils.getId();
		socialUserInfo.setUserId(userId);
		// 第三方表
		socialUserMapper.insertUserConnection(socialUserInfo);
		user.setId(userId);
		user.setHeadImg(socialUserInfo.getImageUrl());
		user.setUsername(socialUserInfo.getProviderUserId());
		user.setNickname(socialUserInfo.getDisplayName());
		// 用户表
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userMapper.insertSelective(user);
		//注册环信用户
		try {
			HuanXinUtils.registerUser(userId, restTemplate);
		} catch (Exception e) {
			throw new BusinessException(ResultEnum.REGISTER_ERROR);
		}
		return user;
	}

	@Override
	public User loadUserInfoByUsername(String username) {
		List<User> list = selectByUsername(username);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	/**
	 * 通过用户名查找用户信息
	 * 
	 * @param username
	 * @return
	 */
	private List<User> selectByUsername(String username) {
		Example example = new Example(User.class);
		example.createCriteria().andEqualTo("username", username);
		List<User> list = userMapper.selectByExample(example);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insertMobileUser(String mobile) {
		String userId = IdUtils.getId();
		User user = new User();
		user.setId(userId);
		user.setUsername(mobile);
		user.setNickname(mobile);
		user.setMobile(mobile);
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userMapper.insertSelective(user);
		try {
			HuanXinUtils.registerUser(userId, restTemplate);
		} catch (Exception e) {
			throw new BusinessException(ResultEnum.REGISTER_ERROR);
		}
		return user;
	}

	@Override
	public PageInfo<User> selectBank(Integer page, Integer size) {
		PageHelper.startPage(page,size);
		PageHelper.orderBy("rank desc");
		List<User> list = userMapper.selectAll();
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<Course> selectCourse(Integer page, Integer size, String id, Integer status) {
		PageHelper.startPage(page,size);
		List<Course> list = userMapper.selectUserCourse(id,status);
		PageInfo<Course> pageInfo = new PageInfo<Course>(list);
		return pageInfo;
	}

	@Override
	public void updateUserInfo(User user) {
		user.setUpdateTime(new Date());
		userMapper.updateByPrimaryKeySelective(user);
	}

}
