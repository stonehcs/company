package com.lichi.increaselimit.netloan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.netloan.dao.NetLoadDao;
import com.lichi.increaselimit.netloan.entity.NetLoan;
import com.lichi.increaselimit.netloan.service.NetLoanService;

import tk.mybatis.mapper.entity.Example;

/**
 * 网贷
 * @author majie
 *
 */	
@Service
public class NetLoanServiceImpl implements NetLoanService{

	@Autowired
	private NetLoadDao netLoadDao;
	
	public List<NetLoan> selectAll(Integer type) {
		Example example = new Example(NetLoan.class);
		example.createCriteria().andEqualTo("type",type);
		return netLoadDao.selectByExample(example);
	}

}
