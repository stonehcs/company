package com.lichi.increaselimit.user.service;

import com.lichi.increaselimit.user.entity.UserCourse;

/**
 * 
 * @author Rodger.Young 用户课程
 */
public interface UserCourseService {
	UserCourse selectUserCourse(String userId, Integer courseId);
}
