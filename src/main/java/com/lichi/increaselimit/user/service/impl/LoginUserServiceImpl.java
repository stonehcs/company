package com.lichi.increaselimit.user.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.user.dao.LoginMapper;
import com.lichi.increaselimit.user.entity.LoginUser;
import com.lichi.increaselimit.user.service.LoginUserService;

@Service
public class LoginUserServiceImpl implements LoginUserService{

	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public void addLoginUser(LoginUser login) {
		login.setLoginTime(new Date());
		loginMapper.insertSelective(login);
	}

	@Override
	public void deleteLoginUser(String id) {
		loginMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<LoginUser> getAll() {
		return loginMapper.selectAll();
	}

}
