package com.lichi.increaselimit.netloan.service;

import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;

/**
 * 诊断字典service
 * @author majie
 *
 */
public interface DiagnosisDicService {

	
	/**
	 * 通过诊断类型和金额获取名称
	 * @param money
	 * @param type
	 * @return
	 */
	String getNameByMoney(int money, int type);
	
	/**
	 * 获取诊断结果
	 * @param bankname
	 * @param money
	 * @return
	 */
	DiagnosisResultList getResult(String bankname, int money);
}
