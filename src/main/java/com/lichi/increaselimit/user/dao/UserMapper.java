package com.lichi.increaselimit.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserRank;

/**
 * userdao
 * 
 * @author majie
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	@Select("select * from t_user where mobile=#{mobile}")
	User loadUserInfoByMobile(String mobile);

	@Select("select b.* from t_user a,t_course b,t_user_course c where a.id=c.user_id and b.id = c.course_id and c.user_id = #{id} and c.status = #{status}")
	List<Course> selectUserCourse(@Param(value = "id") String id, @Param(value = "status") Integer status);

	/**
	 * 获取积分排名
	 * @param id
	 * @return
	 */
	@Select("select * from (select @rownum:=@rownum+1  rownum , a.id , a.rank,a.points from t_user a,(SELECT @rownum:=0) r  "
			+ "order by a.rank desc ) t where t.id = #{id}")
	UserRank getRank(String id);
	
	/**
	 * 获取指定排名信息
	 * @param id
	 * @return
	 */
	@Select("select * from (select @rownum:=@rownum+1  rownum , a.id , a.rank,a.points from t_user a,(SELECT @rownum:=0) r  "
			+ "order by a.rank desc ) t where t.rownum = #{rownum}")
	UserRank getRankByRow(Integer rownum);

}
