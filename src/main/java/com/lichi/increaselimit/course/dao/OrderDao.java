package com.lichi.increaselimit.course.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.Order;

@Mapper
public interface OrderDao extends BaseMapper<Order>{

}
