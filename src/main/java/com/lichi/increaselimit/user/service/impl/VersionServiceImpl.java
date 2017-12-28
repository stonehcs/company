package com.lichi.increaselimit.user.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.user.dao.VersionDao;
import com.lichi.increaselimit.user.entity.Version;
import com.lichi.increaselimit.user.service.VersionService;

@Service
public class VersionServiceImpl implements VersionService{
	@Autowired
	private VersionDao versionDao;

	@Override
	public PageInfo<Version> selectAll(Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("create_time desc");
		List<Version> list = versionDao.selectAll();
		PageInfo<Version> info = new PageInfo<>(list);
		return info;
	}

	@Override
	public Version selectOne(Integer id) {
		return versionDao.selectByPrimaryKey(id);
	}

	@Override
	public void insertOne(Version dept) {
		dept.setUpdateTime(new Date());
		dept.setCreateTime(new Date());
		versionDao.insertSelective(dept);
	}

	@Override
	public void deleteOne(Integer id) {
		versionDao.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Version dept) {
		dept.setUpdateTime(new Date());
		versionDao.updateByPrimaryKeySelective(dept);
		
	}

	@Override
	public Version selectNew() {
		PageHelper.orderBy("create_time desc");
		List<Version> list = versionDao.selectAll();
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
