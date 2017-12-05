package com.lichi.increaselimit.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.user.entity.User;

/**
 * userdao
 * @author majie
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{
	
	/**
	 * 获取用户信息
	 * @param username
	 * @return
	 */
	@Select("select * from t_user where mobile=#{mobile}")
	User loadUserInfoByMobile(String mobile);

	@Select("select b.* from t_user a,t_course b,t_user_course c where a.id=c.user_id and b.id = c.course_id and c.user_id = #{id} and c.status = #{status}")
	List<Course> selectUserCourse(@Param(value = "id") String id, @Param(value = "status")Integer status);
}
