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
import com.lichi.increaselimit.course.controller.dto.TeacherDto;
import com.lichi.increaselimit.course.controller.dto.TeacherUpdateDto;
import com.lichi.increaselimit.course.entity.Teacher;
import com.lichi.increaselimit.course.service.TeacherService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 讲师controller
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/teacher")
@Api(description = "讲师")
@Slf4j
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

	@GetMapping("/list")
	@ApiOperation(value = "分页查询查看讲师列表")
	public ResultVo<PageInfo<Teacher>> getTeacherList(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		log.info("分页查询查看讲师列表");
		PageInfo<Teacher> list = teacherService.getTeacherList(page, size);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "查看讲师详情")
	public ResultVo<Teacher> getTeacher(@PathVariable Integer id) {
		log.info("查询所有老师");
		Teacher teacher = teacherService.getTeacher(id);
		return ResultVoUtil.success(teacher);
	}

	@PostMapping
	@ApiOperation(value = "添加讲师")
	public ResultVo<Teacher> addTeacher(@Valid @RequestBody TeacherDto teacherDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			log.error("添加讲师参数错误:{}" + errors);
			return ResultVoUtil.error(1, errors);
		}
		log.info("添加讲师,讲师姓名:{}",teacherDto.getTeachername());
		Teacher teacher = new Teacher();
		BeanUtils.copyProperties(teacherDto, teacher);
		teacherService.addTeacher(teacher);
		return ResultVoUtil.success();
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除讲师")
	public ResultVo<Teacher> deleteTeacher(@PathVariable Integer id) {
		log.info("删除讲师,讲师id:{}",id);
		teacherService.deleteTeacher(id);
		return ResultVoUtil.success();
	}

	@PutMapping
	@ApiOperation(value = "修改讲师")
	public ResultVo<Teacher> updateTeacher(@Valid @RequestBody TeacherUpdateDto teacherDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			log.error("修改讲师参数错误:{}" + errors);
			return ResultVoUtil.error(1, errors);
		}
		log.info("删除讲师,讲师id:{}",teacherDto.getId());
		Teacher teacher = new Teacher();
		BeanUtils.copyProperties(teacherDto, teacher);
		teacherService.updateTeacher(teacher);
		return ResultVoUtil.success();
	}

}
