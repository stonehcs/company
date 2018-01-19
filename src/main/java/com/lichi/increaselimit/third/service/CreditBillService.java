package com.lichi.increaselimit.third.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.third.entity.Credit;
import com.lichi.increaselimit.third.entity.CreditBill;
import com.lichi.increaselimit.third.entity.CreditBillDetail;
import com.lichi.increaselimit.third.entity.CreditBillVo;
import com.lichi.increaselimit.third.entity.UserEmail;

public interface CreditBillService {
	
	/**
	 * 插入数据
	 * @param listvo
	 * @param email 
	 */
	void insert(List<CreditBillVo> listvo, UserEmail email);

	/**
	 * 分页查询信用卡列表
	 * @param userId
	 * @param size 
	 * @param page 
	 * @return
	 */
	PageInfo<CreditBill> selectByUserId(String userId, Integer page, Integer size);
	/**
	 * 分页查询信用卡列表
	 * @param userId
	 * @param size 
	 * @param page 
	 * @return
	 */
	List<Credit> selectByUserId(String userId);

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
	
	/**
	 * 查询信用卡信息
	 */
	Credit getCredit(String userId, String issueBank, String last4digit, String holderName);

	/**
	 * 获取订单详情
	 * @param billId
	 * @param size 
	 * @param page 
	 * @return
	 */
	PageInfo<CreditBillDetail> selectBillDetail(String billId, Integer page, Integer size);

	/**
	 * 手动添加账单 
	 * @param bill
	 */
	void addBill(Credit bill);
	
	/**
	 * 查询对应信用卡信息
	 */
	Credit getCredit(Integer id);

	/**
	 * 添加对应信用卡账单
	 * @param bill
	 */
	void addCardBill(CreditBill bill);
}
