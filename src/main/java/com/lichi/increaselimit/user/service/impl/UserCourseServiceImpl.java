package com.lichi.increaselimit.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichi.increaselimit.user.dao.UserCourseDao;
import com.lichi.increaselimit.user.entity.UserCourse;
import com.lichi.increaselimit.user.service.UserCourseService;

/**
 * 
 * @author majie
 *
 */
@Service
public class UserCourseServiceImpl implements UserCourseService {
	@Autowired
	UserCourseDao userCourseDao;
	
	@Override
	public UserCourse selectUserCourse(String userId, Integer courseId) {
		return userCourseDao.selectUserCourse(userId, courseId);
	}

}
