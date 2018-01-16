package com.lichi.increaselimit.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.user.dao.VipLevelDao;
import com.lichi.increaselimit.user.entity.VipLevel;
import com.lichi.increaselimit.user.service.VipLevelService;

@Service
public class VipLevelServiceImpl implements VipLevelService {
	
	@Autowired
	private VipLevelDao vipLevelDao;

	@Override
	public VipLevel getLevelPercent(Integer vipLevel) {
		return vipLevelDao.selectByPrimaryKey(vipLevel);
	}
	
	
}
