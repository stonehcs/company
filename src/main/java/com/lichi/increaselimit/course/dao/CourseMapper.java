package com.lichi.increaselimit.course.dao;

import org.apache.ibatis.annotations.Mapper;
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
}
