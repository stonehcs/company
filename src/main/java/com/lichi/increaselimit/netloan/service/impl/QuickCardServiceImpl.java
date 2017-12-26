package com.lichi.increaselimit.netloan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.netloan.dao.QuickCardDao;
import com.lichi.increaselimit.netloan.entity.QuickCard;
import com.lichi.increaselimit.netloan.service.QuickCardService;

/**
 * 网贷
 * @author majie
 *
 */	
@Service
public class QuickCardServiceImpl implements QuickCardService{

	@Autowired
	private QuickCardDao quickCardDao;
	
	public List<QuickCard> selectAll() {
		return quickCardDao.selectAll();
	}

}
