package com.lichi.increaselimit.user.controller;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;
import com.lichi.increaselimit.security.UserUtils;
import com.lichi.increaselimit.user.controller.dto.UserUpdateDto;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 测试controller
 * 
 * @author majie
 *
 */
@Api(description = "我的")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private DiagnosisResultService diagnosisResultService;

	/**
	 * 获取当前用户信息
	 */
	@GetMapping
	@ApiOperation("获取当前用户信息")
	public ResultVo<User> getCurrentUser(HttpServletRequest request)
			throws SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {

		User user = UserUtils.getUserInfo();
		
		return ResultVoUtil.success(user);
	}

	@GetMapping("/rank")
	@ApiOperation("获取用户排行榜")
	public ResultVo<PageInfo<User>> getUserRank(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {

		PageInfo<User> user = userService.selectBank(page, size);

		return ResultVoUtil.success(user);
	}

	@GetMapping("/course")
	@ApiOperation("获取用户课程")
	public ResultVo<PageInfo<Course>> getUserCourse(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "状态 0报名  1 付费", required = true) @RequestParam Integer status,
			@ApiParam(value = "用户id", required = true) @RequestParam String id) {

		PageInfo<Course> userCourse = userService.selectCourse(page, size, id, status);
		return ResultVoUtil.success(userCourse);
	}

	@GetMapping("/card")
	@ApiOperation("刷卡任务")
	public ResultVo<PageInfo<DiagnosisResult>> getUserCourse(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "用户id", required = true) @RequestParam String id) {

		PageInfo<DiagnosisResult> result = diagnosisResultService.getCardTask(page, size, id);
		return ResultVoUtil.success(result);
	}

	@PutMapping
	@ApiOperation("修改用户信息")
	public ResultVo<Object> updateUserInfo(@Valid @RequestBody UserUpdateDto dto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		userService.updateUserInfo(user);
		return ResultVoUtil.success();
	}
}
