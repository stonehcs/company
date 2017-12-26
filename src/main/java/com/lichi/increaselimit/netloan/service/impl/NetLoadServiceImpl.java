package com.lichi.increaselimit.netloan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.netloan.dao.NetLoadDao;
import com.lichi.increaselimit.netloan.entity.NetLoad;
import com.lichi.increaselimit.netloan.service.NetLoadService;

/**
 * 网贷
 * @author majie
 *
 */	
@Service
public class NetLoadServiceImpl implements NetLoadService{

	@Autowired
	private NetLoadDao netLoadDao;
	
	public List<NetLoad> selectAll() {
		return netLoadDao.selectAll();
	}

}
