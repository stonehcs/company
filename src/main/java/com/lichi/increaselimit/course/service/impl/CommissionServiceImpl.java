package com.lichi.increaselimit.course.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.course.dao.CommissionDao;
import com.lichi.increaselimit.course.entity.Commission;
import com.lichi.increaselimit.course.service.CommissionService;

@Service
public class CommissionServiceImpl implements CommissionService{

	@Autowired
	private CommissionDao  commissionDao;

	@Override
	public void insert(Commission commission) {
		commission.setCreateTime(new Date());
		commissionDao.insert(commission);
	}
	
}
