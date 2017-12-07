package com.lichi.increaselimit.course.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.lichi.increaselimit.course.dao.CourseMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.course.service.CourseService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseMapper courseMapper;

	@Override
	public List<Course> getCourseList(Integer page, Integer size, Integer locationId) {
		PageHelper.startPage(page, size);
		Example example = new Example(Course.class);
		example.createCriteria().andEqualTo("locationId", locationId);
		List<Course> list = courseMapper.selectByExample(example);
		return list;
	}

	@Override
	public CourseVo getCourse(Integer id) {

		return courseMapper.selectCourseDetails(id);
	}

	@Override
	public void addCourse(Course course) {
		course.setCreateTime(new Date());
		courseMapper.insertSelective(course);
	}

	@Override
	public void deleteCourse(Integer id) {
		courseMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateCourse(Course course) {
		course.setUpdateTime(new Date());
		courseMapper.updateByPrimaryKeySelective(course);
	}

	@Override
	public void updateCourseTimes(Integer id) {
		courseMapper.updateCourseTimes(id);
	}

}
