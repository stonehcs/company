package com.lichi.increaselimit.user.controller;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.security.UserUtils;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

	/**
	 * 获取当前用户信息
	 */
	@GetMapping
	@ApiOperation("获取当前用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "string", paramType = "header", defaultValue = "bearer ") })
	public ResultVo<User> getCurrentUser(HttpServletRequest request)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {

		String username = UserUtils.getUsername();
		User user = userService.loadUserInfoByUsername(username);

		return ResultVoUtil.success(user);
	}

	@GetMapping("/rank")
	@ApiOperation("获取用户排行榜")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "string", paramType = "header", defaultValue = "bearer ") })
	public ResultVo<PageInfo<User>> getUserRank(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size){
		
		PageInfo<User> user = userService.selectBank(page,size);
		
		return ResultVoUtil.success(user);
	}
	
	@GetMapping("/course")
	@ApiOperation("获取用户课程")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "string", paramType = "header", defaultValue = "bearer ") })
	public ResultVo<PageInfo<Course>> getUserCourse(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "状态 0报名  1 付费", required = true) @RequestParam Integer status,
			@ApiParam(value = "用户id", required = true) @RequestParam String id){
		
		PageInfo<Course> userCourse = userService.selectCourse(page,size,id,status);
		return ResultVoUtil.success(userCourse);
	}

}
