package com.lichi.increaselimit.user.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.utils.StringUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.netloan.entity.CardTaskCount;
import com.lichi.increaselimit.netloan.entity.DiagnosisResult;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.user.controller.dto.UserUpdateDto;
import com.lichi.increaselimit.user.entity.CourseCount;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserRank;
import com.lichi.increaselimit.user.entity.UserVo;
import com.lichi.increaselimit.user.entity.VipLevel;
import com.lichi.increaselimit.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试controller
 * 
 * @author majie
 *
 */
@Api(description = "我的")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private DiagnosisResultService diagnosisResultService;

	@Autowired
	private RedisUtils redisUtils;

	@GetMapping
	@ApiOperation("获取当前用户信息")
	public ResultVo<Object> getCurrentUser(@RequestHeader("token") String token) {
		
		log.info("当前用户token:" + token);

		String string = redisUtils.get(Constants.LOGIN_USER + token);
		
		if(StringUtils.isBlank(string)) {
			throw new BusinessException(ResultEnum.LOGIN_TIME_OUT);
		}
		UserVo vo = JSONObject.parseObject(string, UserVo.class);
		VipLevel level = userService.getLevel(vo.getVipLevel());
		vo.setLevelName(level.getLevelName());
		return ResultVoUtil.success(vo);

	}

	@GetMapping("/rank/ranking")
	@ApiOperation("获取当前用户名次")
	public ResultVo<UserRank> getCurrentUserRank(@ApiParam(value = "用户id", required = true) @RequestParam String id) {
		
		log.info("获取当前用户名次,用户id:{}",id);
		UserRank userRank = userService.getUserRank(id);
		return ResultVoUtil.success(userRank);
	}

	@GetMapping("/rank")
	@ApiOperation("获取所有用户排行榜")
	public ResultVo<PageInfo<User>> getUserRank(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		log.info("获取所有用户排行榜");
		PageInfo<User> user = userService.selectBank(page, size);

		return ResultVoUtil.success(user);
	}

	@GetMapping("/course")
	@ApiOperation("获取用户课程")
	public ResultVo<PageInfo<CourseVo>> getUserCourse(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "状态 0报名  1 付费", required = true) @RequestParam Integer status,
			@ApiParam(value = "用户id", required = true) @RequestParam String id) {
		log.info("获取用户课程,用户id:{},课程状态:{}",id,status);
		PageInfo<CourseVo> userCourse = userService.selectCourse(page, size, id, status);
		return ResultVoUtil.success(userCourse);
	}

	@GetMapping("/mycourse")
	@ApiOperation("我的课程")
	public ResultVo<CourseCount> getMyCourse(@ApiParam(value = "用户id", required = true) @RequestParam String id) {
		log.info("我的课程,用户id:{}",id);
		CourseCount userCourse = userService.getMyCourse(id);
		return ResultVoUtil.success(userCourse);
	}

	@GetMapping("/card")
	@ApiOperation("刷卡任务")
	public ResultVo<PageInfo<DiagnosisResult>> getCardTask(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@ApiParam(value = "状态id", required = false) @RequestParam(required = false) Integer status,
			@ApiParam(value = "用户id", required = true) @RequestParam String id) {
		log.info("我的刷卡任务,用户id:{}",id);
		PageInfo<DiagnosisResult> result = diagnosisResultService.getCardTask(page, size, id, status);
		return ResultVoUtil.success(result);
	}

	@GetMapping("/task-count")
	@ApiOperation("刷卡任务条数")
	public ResultVo<CardTaskCount> getCardTask(@ApiParam(value = "用户id", required = true) @RequestParam String id) {
		log.info("刷卡任务条数,用户id:{}",id);
		CardTaskCount result = diagnosisResultService.getCardTaskCount(id);
		return ResultVoUtil.success(result);
	}

	@PutMapping
	@ApiOperation("修改用户信息,如果是修改手机要先发验证码")
	public ResultVo<Object> updateUserInfo(@Valid @RequestBody UserUpdateDto dto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			log.error("修改用户信息参数错误:{}",errors);
			return ResultVoUtil.error(1, errors);
		}
		if (!StringUtils.isBlank(dto.getMobile())) {
			if (!StringUtil.ValidateMobile(dto.getMobile())) {
				throw new BusinessException(ResultEnum.MOBILE_ERROR);
			}
			if (StringUtils.isBlank(dto.getCode())) {
				throw new BusinessException(ResultEnum.VALIDATECODE_ERROR);
			}
			String json = redisUtils.get(Constants.MOBILE_REDIS_KEY + dto.getMobile());
			if (StringUtils.isBlank(json)) {
				throw new BusinessException(ResultEnum.CODE_NOT_EXIST);
			}
			ValidateCode code = JSONObject.parseObject(json, ValidateCode.class);
			if (!dto.getCode().equals(code.getCode())) {
				throw new BusinessException(ResultEnum.VALIDATECODE_ERROR);
			}
		}
		log.info("修改用户信息,用户id:{}",dto.getId());
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		userService.updateUserInfo(user);
		return ResultVoUtil.success();
	}
	
	@GetMapping("/logout")
	@ApiOperation("退出登录")
	public ResultVo<User> logout(@RequestHeader String token) {
		log.info("退出登录,用户token:{}",token);
		redisUtils.del(Constants.LOGIN_USER + token);
		return ResultVoUtil.success();
	}
}
