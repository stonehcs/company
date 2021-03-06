package com.lichi.increaselimit.third.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.StringUtil;
import com.lichi.increaselimit.third.dao.BillDao;
import com.lichi.increaselimit.third.dao.CreditBillDao;
import com.lichi.increaselimit.third.dao.CreditBillDetailDao;
import com.lichi.increaselimit.third.dao.UserEmailDao;
import com.lichi.increaselimit.third.entity.Credit;
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

	@Autowired
	private BillDao billDao;

	/**
	 * 插入信用卡订单，和email
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(List<CreditBillVo> listvo, UserEmail email) {
		List<CreditBill> listbill = new ArrayList<>();
		List<Credit> recordList = new ArrayList<>();
		List<CreditBillDetail> details = new ArrayList<>();
		List<String> detailDeleteList = new ArrayList<>();
		
		for (CreditBillVo e : listvo) {
			CreditBill creditBillVo = e.getCreditBill();
			String statementStartDate = creditBillVo.getStatementStartDate();
			String statementEndDate = creditBillVo.getStatementEndDate();
			if(StringUtils.isBlank(statementEndDate) && StringUtils.isBlank(statementStartDate)) {
				continue;
			}
			creditBillVo.setEmail(email.getEmail());
			listbill.add(creditBillVo);
			List<CreditBillDetail> detail = e.getCreditBillDetail();
			detail.stream().forEach(a -> {
				details.add(a);
			});
		};

		Map<String, Map<String, List<CreditBill>>> result = listbill.parallelStream()
				.sorted((a, b) -> b.getStatementEndDate().compareTo(a.getStatementEndDate())).collect(Collectors
						.groupingBy(CreditBill::getIssueBank, Collectors.groupingBy(CreditBill::getLast4digit)));

		result.entrySet().stream().forEach(e -> {
			Map<String, List<CreditBill>> value1 = e.getValue();
			value1.entrySet().stream().forEach(c -> {
				List<CreditBill> value2 = c.getValue();
				Optional<CreditBill> findFirst = value2.stream().findFirst();
				CreditBill creditBill = findFirst.get();

				//插入statementdate不为空的数据
				if(StringUtils.isNotBlank(creditBill.getStatementDate())) {
					Credit credit = new Credit();
					BeanUtils.copyProperties(creditBill, credit);
					credit.setCreateTime(new Date());
					recordList.add(credit);
				}
			});
		});

		if (recordList != null && recordList.size() > 0) {
			Example example = new Example(Credit.class);
			example.createCriteria().andEqualTo("userId", recordList.get(0).getUserId()).andEqualTo("email",
					recordList.get(0).getEmail());
			billDao.deleteByExample(example);
			
			billDao.insertList(recordList);
		}

		userEmailDao.insert(email);

		if (listbill != null && listbill.size() > 0) {
			Example example2 = new Example(CreditBill.class);
			example2.createCriteria().andEqualTo("userId", listbill.get(0).getUserId()).andEqualTo("email",
					listbill.get(0).getEmail());
			
			List<CreditBill> existBills = creditBillDao.selectByExample(example2);
			for (CreditBill creditBill : existBills) {
				detailDeleteList.add(creditBill.getId());
			}
			creditBillDao.deleteByExample(example2);

			if (detailDeleteList != null && detailDeleteList.size() > 0) {
				Example example1 = new Example(CreditBillDetail.class);
				example1.createCriteria().andIn("creditBillId", detailDeleteList);
				creditBillDetailDao.deleteByExample(example1);
			}
			creditBillDao.insertBatch(listbill);
		}

		if (details != null && details.size() > 0) {
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
		date = date.minusDays(date.getDayOfMonth() - 1);
		List<CreditBill> list = creditBillDao.selectByUserId(userId, date);
		list.stream().forEach(e -> {
			if (!StringUtils.isBlank(e.getPaymentDueDate())) {
				LocalDate parse = LocalDate.parse(e.getPaymentDueDate());
				long until = LocalDate.now().until(parse, ChronoUnit.DAYS);
				e.setPayDay((int) until);
			}
			if (!StringUtils.isBlank(e.getStatementDate()) && StringUtils.isBlank(e.getPaymentDueDate())) {
				LocalDate parse = LocalDate.parse(e.getStatementDate());
				long until = parse.until(LocalDate.now(), ChronoUnit.DAYS);
				e.setBillDay((int) until);
			}
		});

		// 排序
		list.sort((a, b) -> a.getPayDay().compareTo(b.getPayDay()));
		list.sort((a, b) -> a.getBillDay().compareTo(b.getBillDay()));
		PageInfo<CreditBill> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<CreditBill> selectBank(String userId, String issueBank, String holderName, String last4digit,
			Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("payment_due_date desc");
		Example example = new Example(CreditBill.class);
		example.createCriteria().andEqualTo("userId", userId).andEqualTo("issueBank", issueBank)
				.andEqualTo("last4digit", last4digit).andEqualTo("holderName", holderName);
		List<CreditBill> list = creditBillDao.selectByExample(example);
		PageInfo<CreditBill> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<CreditBillDetail> selectBillDetail(String billId, Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("statement_end_date desc,trans_date desc");
		Example example = new Example(CreditBillDetail.class);
		example.createCriteria().andEqualTo("creditBillId", billId);
		List<CreditBillDetail> list = creditBillDetailDao.selectByExample(example);
		PageInfo<CreditBillDetail> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	/**
	 * 通过用户名获取该用户下面邮箱绑定的所有信用卡信息
	 */
	@Override
	public List<Credit> selectByUserId(String userId) {
		Example example = new Example(Credit.class);
		example.createCriteria().andEqualTo("userId", userId);
		List<Credit> list = billDao.selectByExample(example);
		
		for (Credit credit : list) {

 			String paymentDueDate = credit.getPaymentDueDate();
 			String statementDate = credit.getStatementDate();
 			//如果两个都为空,就为简版,不查
 			if(StringUtils.isBlank(paymentDueDate) && StringUtils.isBlank(statementDate) ) {
 				continue;
 			}
 			if(StringUtils.isNotBlank(paymentDueDate) && StringUtils.isBlank(statementDate) ) {
 				statementDate = LocalDate.parse(paymentDueDate).minus(credit.getFreeDay(),ChronoUnit.DAYS).toString();
 			}
 			//无法算出的给默认20天
 			if(StringUtils.isBlank(paymentDueDate) && StringUtils.isNotBlank(statementDate) ) {
 				paymentDueDate = LocalDate.parse(statementDate).plus(20,ChronoUnit.DAYS).toString();
 			}
			paymentDueDate = StringUtil.dateFormat(paymentDueDate);
			credit.setPaymentDueDate(paymentDueDate);
			statementDate = StringUtil.dateFormat(statementDate);
			credit.setStatementDate(statementDate);
			LocalDate parse = LocalDate.parse(paymentDueDate);
			int dayOfMonth = parse.getDayOfMonth();
			int now = LocalDate.now().getDayOfMonth();
			// long until = LocalDate.now().until(parse, ChronoUnit.DAYS);
			int until = dayOfMonth - now;
			credit.setPayDay(until);
			
			LocalDate parse2 = LocalDate.parse(statementDate);
			int dayOfMonth2 = parse2.getDayOfMonth();
			// long until2 = parse2.until(LocalDate.now(), ChronoUnit.DAYS);
			int until2 = dayOfMonth2 - now;
			credit.setBillDay(until2);
			
			if (until < 0 && until2 < 0) {
				int until3 = dayOfMonth + 30 -now;
				int until4 = dayOfMonth2 + 30 - now;
				if(until4 - until3 > 0) {
					credit.setBillDay(- until4);
					credit.setPayDay(until3);
				}else {
					credit.setBillDay(until4);
					credit.setPayDay(-until3);
				}
			}
			else if (until2 > 0 && until>0) {
				if(until2 - until > 0) {
					credit.setBillDay(- until2);
				}else {
					credit.setPayDay(-until);
				}
			}
		}
		return list;
	}

	@Override
	public void addBill(Credit bill) {
		bill.setCreateTime(new Date());
		String statementDate = bill.getStatementDate();
		String paymentDueDate = bill.getPaymentDueDate();
		paymentDueDate = StringUtil.dateFormat(paymentDueDate);
		statementDate = StringUtil.dateFormat(statementDate);
		LocalDate minusMonths = LocalDate.parse(statementDate).minusMonths(1);
		int days = (int) minusMonths.until(LocalDate.parse(paymentDueDate),ChronoUnit.DAYS);
		
		if(LocalDate.now().getDayOfMonth() - LocalDate.parse(statementDate).getDayOfMonth() > 0) {
			statementDate = LocalDate.parse(statementDate).plusMonths(1).toString();
			bill.setStatementDate(statementDate);
		}
		if(LocalDate.now().getDayOfMonth() - LocalDate.parse(paymentDueDate).getDayOfMonth() > 0) {
			paymentDueDate = LocalDate.parse(paymentDueDate).plusMonths(1).toString();
			bill.setPaymentDueDate(paymentDueDate);
		}
		
		bill.setFreeDay(days);
		bill.setMinPaymentRmb(bill.getBalanceRmb());
		billDao.insertSelective(bill);
	}

	@Override
	public Credit getCredit(Integer id) {
		return billDao.selectByPrimaryKey(id);
	}

	@Override
	public void addCardBill(CreditBill bill) {
		Example example = new Example(CreditBill.class);
		example.createCriteria().andEqualTo("paymentDueDate",bill.getPaymentDueDate())
				.andEqualTo("userId",bill.getUserId()).andEqualTo("email",bill.getEmail())
				.andEqualTo("issueBank",bill.getIssueBank()).andEqualTo("last4digit",bill.getLast4digit());
		List<CreditBill> list = creditBillDao.selectByExample(example);
		if(list == null || list.size() == 0) {
			creditBillDao.insertSelective(bill);
		}else {
			if(null == bill.getEmail()) {
				bill.setId(list.get(0).getId());
				creditBillDao.updateByPrimaryKeySelective(bill);
			}else {
				throw new BusinessException(ResultEnum.HAVE_NO_AUTH);
			}
		}
	}

	@Override
	public Credit getCredit(String userId, String issueBank, String last4digit, String holderName) {
		// 获得信用卡的账单日
		Example example = new Example(Credit.class);
		example.createCriteria().andEqualTo("last4digit", last4digit).andEqualTo("holderName", holderName)
				.andEqualTo("issueBank", issueBank).andEqualTo("userId", userId);
		List<Credit> list2 = billDao.selectByExample(example);
		Credit creditBill = list2.get(0);
		return creditBill;
	}
}
