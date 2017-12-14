package com.lichi.increaselimit.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.CourseVo;

/**
 * Course dao
 * @author majie
 *
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course>{
	
	@Update("update t_course set times = times + 1 where id = #{id}")
	void updateCourseTimes(Integer id);

	/**
	 * 根据课程id查询课程详情 
	 * @param id
	 * @return
	 */
	@Select("select a.*,b.teachername,b.introduce,b.img_url from t_course a left join t_teacher b on a.teacher_id = b.id where a.id=#{id}")
	CourseVo selectCourseDetails(Integer id);

	/**
	 * 查询课程列表
	 * @param locationId
	 * @param userId
	 * @return
	 */
	@Select("select a.*,IFNULL(b.`status`,-1) as status,c.teachername,c.img_url from t_course a left join t_user_course b on a.id = b.course_id and b.user_id = #{userId}" + 
			"LEFT JOIN t_teacher c on a.teacher_id = c.id where location_id = #{locationId} and end_time > NOW()")
	List<CourseVo> selectList(@Param(value = "locationId") Integer locationId, @Param(value = "userId") String userId);

	/**
	 * 课程报名
	 * @param id
	 * @param userId
	 */
	@Insert("insert into t_user_course values(#{userId},#{id},0)")
	void courseSignUp(@Param(value = "id") Integer id,@Param(value = "userId") String userId);

	/**
	 * 课程付费
	 * @param id
	 * @param userId
	 */
	@Update("update t_user_course set status = 1 where course_id = #{id} and userId = #{userId}")
	void coursePay(@Param(value = "id") Integer id,@Param(value = "userId") String userId);
}
