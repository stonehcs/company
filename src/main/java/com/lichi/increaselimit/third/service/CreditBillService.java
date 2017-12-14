package com.lichi.increaselimit.third.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.third.controller.dto.UserEmailDto;
import com.lichi.increaselimit.third.entity.CreditBill;
import com.lichi.increaselimit.third.entity.CreditBillVo;

public interface CreditBillService {
	
	/**
	 * 插入数据
	 * @param listvo
	 * @param userEmail 
	 */
	void insert(List<CreditBillVo> listvo, UserEmailDto userEmail);

	/**
	 * 分页查询信用卡列表
	 * @param userId
	 * @param size 
	 * @param page 
	 * @return
	 */
	PageInfo<CreditBill> selectByUserId(String userId, Integer page, Integer size);

	/**
	 * 查询对应卡详情
	 * @param userId
	 * @param issueBank
	 * @param last4digit
	 * @param holderName 
	 * @param page
	 * @param size
	 * @return
	 */
	PageInfo<CreditBill> selectBank(String userId, String issueBank, String last4digit, String holderName, Integer page, Integer size);
}
