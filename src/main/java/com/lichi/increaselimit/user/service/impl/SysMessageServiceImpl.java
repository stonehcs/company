package com.lichi.increaselimit.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.user.dao.SysMessageDao;
import com.lichi.increaselimit.user.dao.UserDao;
import com.lichi.increaselimit.user.entity.SysMessage;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.SysMessageService;

@Service
public class SysMessageServiceImpl implements SysMessageService{
	@Autowired
	private SysMessageDao messageDao;
	@Autowired
	private UserDao userDao;

	@Override
	public PageInfo<SysMessage> selectAll(Integer page, Integer size ,String userId) {
		
//		Integer countAll = sysUserMapper.countAll();
//		page = page <= 0 ? 1 : page;
//		page = page > countAll/size + 1 ? countAll/size + 1 : page;
//		
//		int start = (page-1)*size;
//		int end = page*size - (page-1)*size ;
		User key = userDao.selectByPrimaryKey(userId);
		PageHelper.startPage(page, size);
		PageHelper.orderBy("create_time desc");
		List<SysMessage> list = messageDao.selectAllMessage(userId, key.getVipLevel());
		PageInfo<SysMessage> info = new PageInfo<>(list);
		return info;
	}

	@Override
	public SysMessage selectOne(Integer id) {
		return messageDao.selectByPrimaryKey(id);
	}

	@Override
	public List<SysMessage> selectList(String userId) {
		User key = userDao.selectByPrimaryKey(userId);
		PageHelper.orderBy("create_time desc");
		List<SysMessage> list = messageDao.selectAllMessage(userId, key.getVipLevel());
		return list;
	}

}
