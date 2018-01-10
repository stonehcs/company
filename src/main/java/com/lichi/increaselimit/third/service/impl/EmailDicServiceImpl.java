package com.lichi.increaselimit.third.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.third.dao.EmailDicDao;
import com.lichi.increaselimit.third.entity.EmailDic;
import com.lichi.increaselimit.third.service.EmailDicService;

@Service
public class EmailDicServiceImpl implements EmailDicService{

	@Autowired
	private EmailDicDao emailDicDao;
	
	@Override
	public List<EmailDic> getAllEmailDic() {
		return emailDicDao.selectAll();
	}

}
