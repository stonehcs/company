package com.lichi.increaselimit.netloan.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.netloan.entity.CardTaskCount;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;

/**
 * 诊断结果service
 * @author majie
 *
 */
public interface DiagnosisResultService {
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertList(List<DiagnosisResult> list);

	/**
	 * 通过用户名和银行卡号获得诊断结果
	 * @param userId
	 * @param bankname
	 * @param last4digit 
	 * @return
	 */
	DiagnosisResultList getResult(String userId, String bankname, String last4digit);

	/**
	 * 更新
	 * @param it
	 * @param money
	 * @return
	 */
	DiagnosisResultList update(Integer id, Double money);

	/**
	 * 获取刷卡任务
	 * @param page
	 * @param size
	 * @param id
	 * @param status 
	 * @return
	 */
	PageInfo<DiagnosisResult> getCardTask(Integer page, Integer size, String id, Integer status);

	/**
	 * 获取刷卡任务数量
	 * @param id
	 * @return
	 */
	CardTaskCount getCardTaskCount(String id);

	/**
	 * 更新完成状态
	 * @param id
	 * @param status
	 */
	void updateStatus(Integer id, Integer status);
}
