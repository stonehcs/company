package com.lichi.increaselimit.course.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.CourseVo;

/**
 * CourseService
 * @author majie
 *
 */
public interface CourseService {

	/**
	 * 查找该地点下的所有课程
	 * @param locationId
	 * @param locationId2 
	 * @param size 
	 * @return
	 */
	PageInfo<CourseVo> getCourseList(Integer page, Integer size, Integer locationId,String userId);

	/**
	 * 根据id查看课程
	 * @return
	 */
	CourseVo getCourse(Integer id);
	
	/**
	 * 新增课程
	 */
	void addCourse(Course course);

	/**
	 * 删除课程
	 * @param id
	 */
	void deleteCourse(Integer id);

	/**
	 * 更新课程信息
	 * @param course
	 */
	void updateCourse(Course course);

	/**
	 * 更新课程观看次数
	 */
	void updateCourseTimes(Integer id);

	/**
	 * 首页显示的课程
	 * @param page
	 * @param size
	 * @param userId 
	 * @param locationId 
	 * @return
	 */
	PageInfo<Course> getCourseList(Integer page, Integer size);

	/**
	 * 课程报名
	 * @param id
	 * @param userId
	 */
	void courseSignUp(Integer id, String userId);

	/**
	 * 课程付费
	 * @param id
	 * @param userId
	 */
	void coursePay(Integer id, String userId);

}
