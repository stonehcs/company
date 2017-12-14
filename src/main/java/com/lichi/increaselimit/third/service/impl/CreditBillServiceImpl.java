package com.lichi.increaselimit.third.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.third.dao.CreditBillDao;
import com.lichi.increaselimit.third.dao.CreditBillDetailDao;
import com.lichi.increaselimit.third.dao.UserEmailDao;
import com.lichi.increaselimit.third.entity.CreditBill;
import com.lichi.increaselimit.third.entity.CreditBillDetail;
import com.lichi.increaselimit.third.entity.CreditBillVo;
import com.lichi.increaselimit.third.entity.UserEmail;
import com.lichi.increaselimit.third.service.CreditBillService;

import tk.mybatis.mapper.entity.Example;

@Service
public class CreditBillServiceImpl implements CreditBillService {

	@Autowired
	private CreditBillDao creditBillDao;

	@Autowired
	private CreditBillDetailDao creditBillDetailDao;

	@Autowired
	private UserEmailDao userEmailDao;

	/**
	 * 插入信用卡订单，和email
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(List<CreditBillVo> listvo, UserEmail email) {
		List<CreditBill> listbill = new ArrayList<>();
		List<CreditBillDetail> details = new ArrayList<>();
		
		listvo.stream().forEach(e -> {
			CreditBill creditBillVo = e.getCreditBill();
			listbill.add(creditBillVo);
			List<CreditBillDetail> detail = e.getCreditBillDetail();
			detail.stream().forEach(a -> {
				details.add(a);
			});
		});
		userEmailDao.insert(email);
		creditBillDao.insertBatch(listbill);
		if(details != null && details.size() > 0) {
			creditBillDetailDao.insertList(details);
		}
	}

	/**
	 * 还款日当前时间或者出账日大于上个1号
	 */
	@Override
	public PageInfo<CreditBill> selectByUserId(String userId, Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("payment_due_date asc , statement_date desc");
		LocalDate date = LocalDate.now().minusMonths(1);
		date = date.minusDays(date.getDayOfMonth()-1);
		List<CreditBill> list = creditBillDao.selectByUserId(userId,date);
		list.stream().forEach( e -> {
			if(!StringUtils.isBlank(e.getPaymentDueDate())) {
				LocalDate parse = LocalDate.parse(e.getPaymentDueDate());
				long until = LocalDate.now().until(parse, ChronoUnit.DAYS);
				e.setBillDay((int)until);
			}
			if(!StringUtils.isBlank(e.getStatementDate()) && StringUtils.isBlank(e.getPaymentDueDate())) {
				LocalDate parse = LocalDate.parse(e.getStatementDate());
				long until = parse.until(LocalDate.now(), ChronoUnit.DAYS);
				e.setPayDay((int)until);
			}
		});
		
//		list = list.stream().sorted(Comparator.comparing(null, null)).collect(Collectors.toList());
		PageInfo<CreditBill> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<CreditBill> selectBank(String userId, String issueBank,String holderName,String last4digit, Integer page,
			Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("payment_due_date desc");
		Example example = new Example(CreditBill.class);
		example.createCriteria().andEqualTo("userId", userId).andEqualTo("issueBank", issueBank)
				.andEqualTo("last4digit", last4digit).andEqualTo("holderName", holderName);
		List<CreditBill> list = creditBillDao.selectByExample(example);
		PageInfo<CreditBill> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
}
