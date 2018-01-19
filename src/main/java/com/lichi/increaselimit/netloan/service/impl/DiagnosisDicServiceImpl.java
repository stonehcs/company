package com.lichi.increaselimit.netloan.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.utils.RandomUtils;
import com.lichi.increaselimit.netloan.dao.DiagnosisDicDao;
import com.lichi.increaselimit.netloan.dao.DiagnosisMoudleDao;
import com.lichi.increaselimit.netloan.dao.DiagnosisResultDao;
import com.lichi.increaselimit.netloan.entity.DiagnosisDic;
import com.lichi.increaselimit.netloan.entity.DiagnosisMoudle;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.exception.CardException;
import com.lichi.increaselimit.netloan.service.DiagnosisDicService;
import com.lichi.increaselimit.third.dao.BillDao;
import com.lichi.increaselimit.third.entity.Credit;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 诊断service
 * 
 * @author majie
 *
 */
@Service
@Slf4j
public class DiagnosisDicServiceImpl implements DiagnosisDicService {

	@Autowired
	private DiagnosisDicDao diagnosisDicMapper;
	@Autowired
	private DiagnosisMoudleDao diagnosisMoudleMapper;
	@Autowired
	private DiagnosisResultDao diagnosisResultMapper;
	@Autowired
	private BillDao billDao;

	private List<LocalDate> OTHERDAYLIST = new ArrayList<>();

	private Map<LocalDate, Integer> WEEKSMAP = new HashMap<>();

	@Override
	public String getNameByMoney(int money, int type) {
		/**
		 * 如果固定且末尾是不是0,就不能是加油,是0才能使加油
		 */
		Example example = new Example(DiagnosisDic.class);
		if (type == 1 && money % 10 != 0) {
			example.createCriteria().andEqualTo("type", type).andCondition("min <= ", money)
					.andCondition("max >= ", money).andNotEqualTo("name", "加油站");
		} else if (type == 1 && money % 10 == 0) {
			example.createCriteria().andEqualTo("type", type).andCondition("min <= ", money)
					.andCondition("max >= ", money).andEqualTo("name", "加油站");
		} else {
			example.createCriteria().andEqualTo("type", type).andCondition("min <= ", money).andCondition("max >= ",
					money);
		}
		List<DiagnosisDic> list = diagnosisDicMapper.selectByExample(example);
		Collections.shuffle(list);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0).getName();
	}

	/**
	 * 通过银行卡号和类型获得诊断模型
	 * 
	 * @param bankname
	 * @param type
	 * @param last4
	 * @return
	 */
	private DiagnosisMoudle getDiagnosisMoudle(String bankname, int type, String last4) {
		Example example = new Example(DiagnosisMoudle.class);
		example.createCriteria().andEqualTo("bankname", bankname).andEqualTo("type", type);
		List<DiagnosisMoudle> list = diagnosisMoudleMapper.selectByExample(example);
		if (list.isEmpty()) {
			throw new CardException(ResultEnum.NOT_SUPPORT_ERROR, bankname, last4);
		}
		return list.get(0);
	}

	@Override
	public synchronized DiagnosisResultList getResult(String bankname, double money, String last4, String userId,
			String holderName) {

		OTHERDAYLIST.clear();
		WEEKSMAP.clear();

		// 获得信用卡的账单日
		Example example = new Example(Credit.class);
		example.createCriteria().andEqualTo("last4digit", last4).andEqualTo("holderName", holderName)
				.andEqualTo("issueBank", bankname).andEqualTo("userId", userId);
		List<Credit> list2 = billDao.selectByExample(example);
		Credit creditBill = list2.get(0);
		String statementDate = creditBill.getStatementDate();
		int day = Integer.parseInt(statementDate.split("-")[2]);

		LocalDate start = LocalDate.now();
		int year = start.plusMonths(1).getYear();
		int month = start.plusMonths(1).getMonthValue();
		LocalDate endTime = null;
		try {
			endTime = LocalDate.of(year, month, day);
		} catch (Exception e) {
			endTime = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
		}

		// 中间天数
		long diff = (int) start.until(endTime, ChronoUnit.DAYS);

		log.info("账单周期天数:" + diff);

		for (int i = 0; i <= diff; i++) {
			OTHERDAYLIST.add(start.plusDays(i));
		}
		Collections.shuffle(OTHERDAYLIST);
		
		DiagnosisResultList result = new DiagnosisResultList();
		List<DiagnosisResult> list = new ArrayList<>();
		List<DiagnosisResult> percent = new ArrayList<>();

		// 固定模式
		DiagnosisMoudle entity = getDiagnosisMoudle(bankname, 1, last4);

		// 获取随机次数
		Integer rand = RandomUtils.generateRandomArray(entity.getMinTimes(), entity.getMaxTimes());

		log.info("固定次数：" + rand);

		// 获取随机金额
		Set<Integer> random = RandomUtils.generateRandomArray(rand, entity.getMin(), entity.getMax());

		getResult(list, random, result, 1, bankname, last4, userId, holderName, day);

		// 百分比
		DiagnosisMoudle entity2 = getDiagnosisMoudle(bankname, 2, last4);

		// 获取随机次数
		Integer per = RandomUtils.generateRandomArray(entity2.getMinTimes(), entity.getMaxTimes());

		log.info("百分比次数：" + per);

		// 获取百分比金额
		Set<Integer> set = RandomUtils.generateRandomArray(per, entity2.getMinPer(), entity2.getMaxPer(),
				entity2.getMin(), entity2.getMax(), money);

		getResult(percent, set, result, 2, bankname, last4, userId, holderName, day);

		return result;
	}

	/**
	 * 
	 * @param list
	 *            固定方式list or 百分比方式list
	 * @param random
	 *            固定方式得到的金额 or 百分比方式得到的金额
	 * @param result
	 *            返回的结果
	 * @param type
	 *            固定 or 百分比
	 * @param bankname
	 */
	private void getResult(List<DiagnosisResult> list, Set<Integer> random, DiagnosisResultList result, int type,
			String bankname, String last4, String userId, String holderName, int day) {

		for (Integer integer : random) {
			String name = getNameByMoney(integer, type);
			if (StringUtils.isBlank(name)) {
				continue;
			}
			DiagnosisResult diagnosisResult = new DiagnosisResult();
			diagnosisResult.setName(name);
			diagnosisResult.setMoney((double) integer);
			diagnosisResult.setType(type);
			diagnosisResult.setBankname(bankname);
			diagnosisResult.setUserId(userId);
			diagnosisResult.setLast4(last4);
			diagnosisResult.setConsumeMoney(0d);
			diagnosisResult.setStatus(0);
			diagnosisResult.setHolderName(holderName);

			diagnosisResult.setTime(getChance().toString());

			list.add(diagnosisResult);

			if (1 == type) {
				result.setFixed(list);
			} else {
				result.setPercent(list);
			}
		}

		diagnosisResultMapper.insertList(list);

	}

	private LocalDate getChance() {
		LocalDate localDate = null;
		// Random random = new Random();
		// int i = random.nextInt(99);
		// //周末改了是平时两倍
		// if (i >= 0 && i < 66) {
		// Collections.shuffle(WEEKSLIST);
		// localDate = WEEKSLIST.get(0);
		// if(WEEKSLISTFULL.contains(localDate)) {
		// localDate = WEEKSLIST.get(1);
		// if(WEEKSLISTFULL.contains(localDate)) {
		// localDate = WEEKSLIST.get(2);
		// }
		// }
		// while (null != WEEKSMAP.get(localDate) && WEEKSMAP.get(localDate) >= 2) {
		// WEEKSLISTFULL.add(localDate);
		// localDate = getChance();
		// }
		// while (localDate.getDayOfWeek().getValue() == 6 && WEEKSMAP.get(localDate) !=
		// null
		// && WEEKSMAP.get(localDate) <= 2) {
		// if (WEEKSMAP.get(localDate.plusDays(1)) != null
		// && WEEKSMAP.get(localDate.plusDays(1)) + WEEKSMAP.get(localDate) >= 3) {
		// WEEKSLISTFULL.add(localDate);
		// localDate = getChance();
		// } else {
		// break;
		// }
		// }
		// while (localDate.getDayOfWeek().getValue() == 7 && WEEKSMAP.get(localDate) !=
		// null
		// && WEEKSMAP.get(localDate) <= 2) {
		// if (WEEKSMAP.get(localDate.minusDays(1)) != null
		// && WEEKSMAP.get(localDate.minusDays(1)) + WEEKSMAP.get(localDate) >= 3) {
		// WEEKSLISTFULL.add(localDate);
		// localDate = getChance();
		// } else {
		// break;
		// }
		// }
		// WEEKSMAP.put(localDate, WEEKSMAP.get(localDate) == null ? 1 :
		// WEEKSMAP.get(localDate) + 1);
		// if(WEEKSMAP.get(localDate) == 2) {
		// WEEKSLISTFULL.add(localDate);
		// }
		// } else {

		localDate = OTHERDAYLIST.get(0);
		if (localDate.getDayOfWeek().getValue() == 6 || localDate.getDayOfWeek().getValue() == 7) {
			Integer integer = WEEKSMAP.get(localDate);
			if (integer != null && integer == 2) {
				OTHERDAYLIST.remove(0);
				localDate = getChance();
			} else {
				WEEKSMAP.put(localDate, WEEKSMAP.get(localDate) == null ? 1 : WEEKSMAP.get(localDate) + 1);
				if (WEEKSMAP.get(localDate) == 2) {
					OTHERDAYLIST.remove(0);
				}
			}
		} else {
			if (OTHERDAYLIST.isEmpty()) {
				log.info("非周末被消费完了.....");
				localDate = getChance();
			}
			OTHERDAYLIST.remove(0);
		}
		return localDate;
	}

}
