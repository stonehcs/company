package com.lichi.increaselimit.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.user.dao.SysMessageDao;
import com.lichi.increaselimit.user.entity.SysMessage;
import com.lichi.increaselimit.user.service.SysMessageService;

@Service
public class SysMessageServiceImpl implements SysMessageService{
	@Autowired
	private SysMessageDao messageDao;

	@Override
	public PageInfo<SysMessage> selectAll(Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("create_time desc");
		List<SysMessage> list = messageDao.selectAll();
		PageInfo<SysMessage> info = new PageInfo<>(list);
		return info;
	}

	@Override
	public SysMessage selectOne(Integer id) {
		return messageDao.selectByPrimaryKey(id);
	}

	@Override
	public List<SysMessage> selectList() {
		PageHelper.orderBy("create_time desc");
		List<SysMessage> list = messageDao.selectAll();
		return list;
	}

}
