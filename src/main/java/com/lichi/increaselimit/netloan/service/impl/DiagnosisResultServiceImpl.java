package com.lichi.increaselimit.netloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.netloan.dao.DiagnosisResultDao;
import com.lichi.increaselimit.netloan.entity.CardTaskCount;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;

import tk.mybatis.mapper.entity.Example;

/**
 * 诊断结果service
 * 
 * @author majie
 *
 */
@Service
public class DiagnosisResultServiceImpl implements DiagnosisResultService {

	@Autowired
	private DiagnosisResultDao diagnosisResultMapper;

	public void insertList(List<DiagnosisResult> list) {
		diagnosisResultMapper.insertList(list);
	}

	@Override
	public DiagnosisResultList getResult(String userId, String bankname,String last4digit) {
		DiagnosisResultList result = new DiagnosisResultList();
		Example example = new Example(DiagnosisResult.class);
		example.createCriteria().andEqualTo("userId", userId).andEqualTo("bankname", bankname).andEqualTo("last4", last4digit);
		List<DiagnosisResult> list = diagnosisResultMapper.selectByExample(example);
		List<DiagnosisResult> fixed = list.stream().filter(e -> 1 == e.getType()).collect(Collectors.toList());
		List<DiagnosisResult> percent = list.stream().filter(e -> 2 == e.getType()).collect(Collectors.toList());
		result.setFixed(fixed);
		result.setPercent(percent);
		return result;
	}

	@Override
	public void update(Integer id, Double money) {
		DiagnosisResult record = new DiagnosisResult();
		record.setId(id);
		record.setConsumeMoney(money);
		record.setStatus(1);
		diagnosisResultMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public PageInfo<DiagnosisResult> getCardTask(Integer page, Integer size, String id,Integer status) {
		Example example = new Example(DiagnosisResult.class);
		if(status != null) {
			PageHelper.startPage(page, size);
			PageHelper.orderBy("time desc");
			example.createCriteria().andEqualTo("userId", id).andEqualTo("status", status);
		}else {
			PageHelper.startPage(page, size);
			PageHelper.orderBy("time desc");
			example.createCriteria().andEqualTo("userId", id);
		}
		List<DiagnosisResult> list = diagnosisResultMapper.selectByExample(example);
		PageInfo<DiagnosisResult> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public CardTaskCount getCardTaskCount(String id) {
		CardTaskCount cardTaskCount = new CardTaskCount();
		Example example = new Example(DiagnosisResult.class);
		example.createCriteria().andEqualTo("userId", id);
		List<DiagnosisResult> list = diagnosisResultMapper.selectByExample(example);
		long undone = list.stream().filter(e -> e.getStatus() == 0).count();
		long done = list.stream().filter(e -> e.getStatus() == 1).count();
		cardTaskCount.setTotal(null == list ? 0 : list.size());
		cardTaskCount.setDone((int) done);
		cardTaskCount.setUndone((int) undone);
		return cardTaskCount;
	}

}
