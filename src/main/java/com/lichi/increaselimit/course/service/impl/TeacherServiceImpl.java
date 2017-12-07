package com.lichi.increaselimit.course.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.course.dao.CourseMapper;
import com.lichi.increaselimit.course.dao.TeacherMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.Teacher;
import com.lichi.increaselimit.course.service.TeacherService;

import tk.mybatis.mapper.entity.Example;

@Service
public class TeacherServiceImpl implements TeacherService{

	@Autowired
	private TeacherMapper mapper;
	@Autowired
	private CourseMapper courseMapper;
	
	@Override
	public List<Teacher> getTeacherList(Integer page, Integer size) {
		PageHelper.startPage(page,size);
		return mapper.selectAll();
	}

	@Override
	public Teacher getTeacher(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void addTeacher(Teacher teacher) {
		teacher.setCreateTime(new Date());
		mapper.insertSelective(teacher);
	}

	/**
	 * 删除讲师先验证是否有课程
	 */
	@Override
	public void deleteTeacher(Integer id) {
		Example example = new Example(Course.class);
		example.createCriteria().andEqualTo("teacherId",id);
		List<Course> list = courseMapper.selectByExample(example);
		if(!list.isEmpty()) {
			throw new BusinessException(ResultEnum.COURSE_NOT_EMPTY);
		}
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		teacher.setUpdateTime(new Date());
		mapper.updateByPrimaryKeySelective(teacher);
	}

}
