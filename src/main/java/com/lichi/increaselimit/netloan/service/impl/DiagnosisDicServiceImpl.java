package com.lichi.increaselimit.netloan.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.RandomUtils;
import com.lichi.increaselimit.netloan.dao.DiagnosisDicMapper;
import com.lichi.increaselimit.netloan.dao.DiagnosisMoudleMapper;
import com.lichi.increaselimit.netloan.dao.DiagnosisResultMapper;
import com.lichi.increaselimit.netloan.entity.DiagnosisDic;
import com.lichi.increaselimit.netloan.entity.DiagnosisMoudle;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.service.DiagnosisDicService;

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
	private DiagnosisDicMapper diagnosisDicMapper;
	@Autowired
	private DiagnosisMoudleMapper diagnosisMoudleMapper;
	@Autowired
	private DiagnosisResultMapper diagnosisResultMapper;

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
	 * @return
	 */
	private DiagnosisMoudle getDiagnosisMoudle(String bankname, int type) {
		Example example = new Example(DiagnosisMoudle.class);
		example.createCriteria().andEqualTo("bankname", bankname).andEqualTo("type", type);
		List<DiagnosisMoudle> list = diagnosisMoudleMapper.selectByExample(example);
		if (list.isEmpty()) {
			throw new BusinessException(ResultEnum.NOT_SUPPORT_ERROR);
		}
		return list.get(0);
	}

	@Override
	public DiagnosisResultList getResult(String bankname, int money) {

		DiagnosisResultList result = new DiagnosisResultList();
		List<DiagnosisResult> list = new ArrayList<>();
		List<DiagnosisResult> percent = new ArrayList<>();

		// 固定模式
		DiagnosisMoudle entity = getDiagnosisMoudle(bankname, 1);

		// 获取随机次数
		Integer rand = RandomUtils.generateRandomArray(entity.getMinTimes(), entity.getMaxTimes());

		log.info("固定次数：" + rand);

		// 获取随机金额
		Set<Integer> random = RandomUtils.generateRandomArray(rand, entity.getMin(), entity.getMax());

		getResult(list, random, result, 1, bankname);

		// 百分比
		DiagnosisMoudle entity2 = getDiagnosisMoudle(bankname, 2);

		// 获取随机次数
		Integer per = RandomUtils.generateRandomArray(entity2.getMinTimes(), entity.getMaxTimes());

		log.info("百分比次数：" + per);

		// 获取百分比金额
		Set<Integer> set = RandomUtils.generateRandomArray(per, entity2.getMinPer(), entity2.getMaxPer(),
				entity2.getMin(), entity2.getMax(), money);

		getResult(percent, set, result, 2, bankname);

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
	private void getResult(List<DiagnosisResult> list, Set<Integer> random, DiagnosisResultList result, int type, String bankname) {
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
			// TODO:这里先写死
			diagnosisResult.setUserId("11111111111");
			list.add(diagnosisResult);

			if (1 == type) {
				result.setFixed(list);
			} else {
				result.setPercent(list);
			}
		}

		diagnosisResultMapper.insertList(list);

	}

}
