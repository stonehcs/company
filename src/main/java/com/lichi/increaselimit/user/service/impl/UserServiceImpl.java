package com.lichi.increaselimit.user.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lichi.increaselimit.common.utils.UserIdUtils;
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insertSocialUser(SocialUserInfo socialUserInfo) {
		User user = new User();
		String userId = UserIdUtils.getUserId();
		socialUserInfo.setUserId(userId);
		// 第三方表
		socialUserMapper.insertUserConnection(socialUserInfo);
		user.setId(userId);
		user.setHeadImg(socialUserInfo.getImageUrl());
		user.setUsername(socialUserInfo.getProviderUserId());
		user.setNickname(socialUserInfo.getDisplayName());
		// 用户表
		userMapper.insert(user);
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
	public User insertMobileUser(String mobile) {
		String userId = UserIdUtils.getUserId();
		User user = new User();
		user.setId(userId);
		user.setUsername(mobile);
		user.setMobile(mobile);
		userMapper.insert(user);
		return user;
	}
}
