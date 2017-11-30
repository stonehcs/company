package com.lichi.increaselimit.netloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.netloan.dao.DiagnosisResultMapper;
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
	private DiagnosisResultMapper diagnosisResultMapper;

	public void insertList(List<DiagnosisResult> list) {
		diagnosisResultMapper.insertList(list);
	}

	@Override
	public DiagnosisResultList getResult(String userId, String bankname) {
		DiagnosisResultList result = new DiagnosisResultList();
		Example example = new Example(DiagnosisResult.class);
		example.createCriteria().andEqualTo("userId", userId).andEqualTo("bankname", bankname);
		List<DiagnosisResult> list = diagnosisResultMapper.selectByExample(example);
		List<DiagnosisResult> fixed = list.stream().filter(e -> 1 == e.getType()).collect(Collectors.toList());
		List<DiagnosisResult> percent = list.stream().filter(e -> 2 == e.getType()).collect(Collectors.toList());
		result.setFixed(fixed);
		result.setPercent(percent);
		return result;
	}

	@Override
	public DiagnosisResultList update(Integer id, Double money) {
		DiagnosisResult record = new DiagnosisResult();
		record.setId(id);
		record.setMoney(money);
		diagnosisResultMapper.updateByPrimaryKeySelective(record);
		return null;
	}

}
