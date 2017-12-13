package com.lichi.increaselimit.third.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.third.entity.CreditBill;

@Mapper
public interface CreditBillDao extends BaseMapper<CreditBill>{

	void insertBatch(List<CreditBill> listbill);
}
