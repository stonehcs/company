package com.lichi.increaselimit.third.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.third.entity.CreditBill;

@Mapper
public interface CreditBillDao extends BaseMapper<CreditBill>{

	void insertBatch(List<CreditBill> listbill);

	@Select("select * from t_credit_bill where user_id = #{userId} and (payment_due_date > now () or statement_date > #{date})")
	List<CreditBill> selectByUserId(@Param(value = "userId") String userId, @Param(value = "date")LocalDate date);
}
