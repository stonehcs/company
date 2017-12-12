package com.lichi.increaselimit.third.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.third.dao.UserEmailDao;
import com.lichi.increaselimit.third.entity.UserEmail;
import com.lichi.increaselimit.third.service.UserEmailService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserEmailServiceImpl implements UserEmailService {

	@Autowired
	private UserEmailDao userEmailDao;
	
	public void insert(UserEmail userEmail) {
		userEmailDao.insert(userEmail);
	}

	@Override
	public List<UserEmail> getList(String userId) {
		Example example = new Example(UserEmail.class);
		example.createCriteria().andEqualTo("userId",userId);
		return userEmailDao.selectByExample(example);
	}

	@Override
	public UserEmail selectByPrimaryKey(String username) {
		return userEmailDao.selectByPrimaryKey(username);
	}

}
