package com.lichi.increaselimit.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.course.dao.OrderDao;
import com.lichi.increaselimit.course.entity.Order;
import com.lichi.increaselimit.course.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDao orderDao;
	
	@Override
	public void insert(Order order) {
		orderDao.insertSelective(order);
	}

}
