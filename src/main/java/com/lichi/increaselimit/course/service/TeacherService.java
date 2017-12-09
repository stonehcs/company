package com.lichi.increaselimit.course.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.course.entity.Teacher;

/**
 * TeacherService
 * @author majie
 *
 */
public interface TeacherService {

	/**
	 * 查看所有老师
	 * @param size 
	 * @param page 
	 * @param locationId
	 * @return
	 */
	PageInfo<Teacher> getTeacherList(Integer page, Integer size);

	/**
	 * 根据id查找老师
	 * @return
	 */
	Teacher getTeacher(Integer id);
	
	/**
	 * 添加老师
	 */
	void addTeacher(Teacher teacher);

	/**
	 * 删除老师
	 * @param id
	 */
	void deleteTeacher(Integer id);

	/**
	 * 更新老师信息
	 * @param Teacher
	 */
	void updateTeacher(Teacher teacher);

}
