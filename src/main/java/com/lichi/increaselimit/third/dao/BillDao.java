package com.lichi.increaselimit.third.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.third.entity.Credit;

@Mapper
public interface BillDao extends BaseMapper<Credit>{

	void updateBatch(List<Credit> updateList);

}
