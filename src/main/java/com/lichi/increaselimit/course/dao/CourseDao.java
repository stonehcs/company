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
 * 
 * @author majie
 *
 */
@Mapper
public interface CourseDao extends BaseMapper<Course> {

	@Update("update t_course set times = times + 1 where id = #{id}")
	void updateCourseTimes(Integer id);

	/**
	 * 根据课程id查询课程详情
	 * 
	 * @param id
	 * @return
	 */
	@Select("select a.*,b.teachername,b.introduce,b.img_url from t_course a left join t_teacher b on a.teacher_id = b.id where a.id=#{id}")
	CourseVo selectCourseDetails(Integer id);

	/**
	 * 查询课程列表
	 * 
	 * @param locationId
	 * @param userId
	 * @return
	 */
	@Select("select a.*,IFNULL(b.`status`,-1) as status,c.teachername,c.img_url from t_course a left join t_user_course b on a.id = b.course_id and b.user_id = #{userId}"
			+ "LEFT JOIN t_teacher c on a.teacher_id = c.id where location_id = #{locationId} and end_time > NOW()")
	List<CourseVo> selectList(@Param(value = "locationId") Integer locationId, @Param(value = "userId") String userId);

	/**
	 * 课程报名
	 * 
	 * @param id
	 * @param userId
	 */
	@Insert("insert into t_user_course values(#{userId},#{id},0)")
	void courseSignUp(@Param(value = "id") Integer id, @Param(value = "userId") String userId);

	/**
	 * 课程付费
	 * 
	 * @param id
	 * @param userId
	 */
	@Update("update t_user_course set status = 1 where course_id = #{id} and user_id = #{userId}")
	void coursePay(@Param(value = "id") Integer id, @Param(value = "userId") String userId);

	/**
	 * 模糊查询
	 * 
	 * @param name
	 * @return
	 */
	@Select("select a.*,b.teachername,b.introduce,b.img_url from t_course a left join t_teacher b on a.teacher_id = b.id "
			+ "where (a.title LIKE concat('%', #{name}, '%') or b.teachername LIKE concat('%', #{name}, '%') )")
	List<CourseVo> selectByLike(@Param(value = "name") String name);

	/**
	 * 获得对应人数
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Select("select count(course_id) from t_user_course where course_id = #{id} and status = #{status}")
	Integer getCount(@Param(value = "id") Integer id, @Param(value = "status") Integer status);

	/**
	 * 首页列表
	 * 
	 * @return
	 */
	@Select("select a.*,b.teachername,b.introduce,b.img_url from t_course a left join t_teacher b on a.teacher_id = b.id where end_time > now()")
	List<CourseVo> selectListIndex();

	@Select("select a.*,b.teachername,b.introduce,b.img_url from t_course a left join t_teacher b on a.teacher_id = b.id where a.location_id=#{locationId} and end_time > now()")
	List<CourseVo> selectByLocationId(Integer locationId);

	/**
	 * 获取我的课程
	 * 
	 * @param id
	 * @param i
	 * @return
	 */
	@Select("select count(*) from t_user_course where user_id = #{id} and status = #{status}")
	Integer getMyCourse(@Param(value = "id") String id, @Param(value = "status") Integer status);

	/**
	 * 查看课程状态
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	@Select("select status from t_user_course where user_id = #{userId} and course_id = #{id}")
	Integer selectStatus(@Param(value = "id") Integer id, @Param(value = "userId") String userId);

	@Select("select a.*,IFNULL(b.`status`,-1) as status,c.teachername,c.img_url "
			+ "from t_course a left join t_user_course b on a.id = b.course_id and b.user_id = #{userId}"
			+ "LEFT JOIN t_teacher c on a.teacher_id = c.id where end_time > NOW()")
	List<CourseVo> selectLoginCourse(String id);
}
