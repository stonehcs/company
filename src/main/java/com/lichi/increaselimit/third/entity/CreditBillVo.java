package com.lichi.increaselimit.third.entity;

import java.util.List;

public class CreditBillVo{
	
	/**
	 * 信用卡账单
	 */
	private CreditBill creditBill;
	/**
	 * 信用卡账单详情
	 */
	private List<CreditBillDetail> CreditBillDetail;

	public List<CreditBillDetail> getCreditBillDetail() {
		return CreditBillDetail;
	}

	public void setCreditBillDetail(List<CreditBillDetail> creditBillDetail) {
		CreditBillDetail = creditBillDetail;
	}

	public CreditBill getCreditBill() {
		return creditBill;
	}

	public void setCreditBill(CreditBill creditBill) {
		this.creditBill = creditBill;
	}
	
	
}
