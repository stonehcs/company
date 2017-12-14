package com.lichi.increaselimit.course.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.course.controller.dto.CourseDto;
import com.lichi.increaselimit.course.controller.dto.CourseUpdateDto;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.course.service.CourseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 课程controller
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/course")
@Api(description = "课程")
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping("/list")
	@ApiOperation(value = "查看课程列表")
	public ResultVo<PageInfo<CourseVo>> getCourseList(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "地区id", required = true) @RequestParam Integer locationId,
			@ApiParam(value = "用户id", required = true) @RequestParam String userId) {
		PageInfo<CourseVo> list = courseService.getCourseList(page, size, locationId,userId);
		return ResultVoUtil.success(list);
	}
	
	@GetMapping("/index-course")
	@ApiOperation(value = "首页课程显示")
	public ResultVo<PageInfo<Course>> getCourseList(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		PageInfo<Course> list = courseService.getCourseList(page, size);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "查看课程详情")
	public ResultVo<CourseVo> getCourse(@PathVariable Integer id) {
		CourseVo course = courseService.getCourse(id);
		return ResultVoUtil.success(course);
	}

	@PostMapping
	@ApiOperation(value = "添加课程")
	public ResultVo<Course> addCourse(@Valid @RequestBody CourseDto courseDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		Course course = new Course();
		BeanUtils.copyProperties(courseDto, course);
		courseService.addCourse(course);
		return ResultVoUtil.success();
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除课程")
	public ResultVo<Course> deleteCourse(@PathVariable Integer id) {
		courseService.deleteCourse(id);
		return ResultVoUtil.success();
	}

	@PutMapping
	@ApiOperation(value = "修改课程")
	public ResultVo<Course> updateCourse(@Valid @RequestBody CourseUpdateDto courseDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		Course course = new Course();
		BeanUtils.copyProperties(courseDto, course);
		courseService.updateCourse(course);
		return ResultVoUtil.success();
	}

	@PutMapping("/watch/{id}")
	@ApiOperation(value = "修改课程观看次数")
	public ResultVo<Course> updateCourse(@PathVariable Integer id) {
		courseService.updateCourseTimes(id);
		return ResultVoUtil.success();
	}
	
	@GetMapping("signUp")
	@ApiOperation(value = "课程报名")
	public ResultVo<Course> signUp(
			@ApiParam(value = "课程id", required = true) @RequestParam Integer id,
			@ApiParam(value = "地区id", required = true) @RequestParam String userId) {
		courseService.courseSignUp(id,userId);
		return ResultVoUtil.success();
	}
	
	@GetMapping("pay")
	@ApiOperation(value = "课程付费")
	public ResultVo<Course> pay(
			@ApiParam(value = "课程id", required = true) @RequestParam Integer id,
			@ApiParam(value = "地区id", required = true) @RequestParam String userId) {
		courseService.coursePay(id,userId);
		return ResultVoUtil.success();
	}
}
