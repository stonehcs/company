package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.user.entity.UserCourse;

/**
 * 
 * @author Rodger.Young
 * 用户课程接口
 */
@Mapper
public interface UserCourseDao {
	/**
	 * 主键查询用户课程
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@Select("select *from t_user_course where user_id=#{userId} and course_id=#{courseId}")
	UserCourse	selectUserCourse(@Param(value = "userId")String userId,@Param(value = "courseId")Integer courseId);
	
	
}
