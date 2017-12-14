package com.lichi.increaselimit.course.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.IdUtils;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.course.controller.dto.SignUpDto;
import com.lichi.increaselimit.course.dao.CourseMapper;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.course.service.CourseService;
import com.lichi.increaselimit.security.authentication.mobile.SmsCodeAuthenticationToken;
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private RedisUtils redisUtils;

	@Override
	public PageInfo<CourseVo> getCourseList(Integer page, Integer size, Integer locationId, String userId) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("start_time asc");
		List<CourseVo> list = courseMapper.selectList(locationId, userId);
		PageInfo<CourseVo> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<Course> getCourseList(Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("start_time asc");
		Example example = new Example(Course.class);
		example.createCriteria().andCondition("end_time >=", new Date());
		List<Course> list = courseMapper.selectByExample(example);
		PageInfo<Course> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public CourseVo getCourse(Integer id) {

		return courseMapper.selectCourseDetails(id);
	}

	@Override
	public void addCourse(Course course) {
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		if (course.getEndTime().getTime() < course.getStartTime().getTime()) {
			throw new BusinessException(ResultEnum.ENDTIME_BIGGER_THEN_STARTTIME);
		}
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

	@Override
	public void coursePay(Integer id, String userId) {
		courseMapper.coursePay(id, userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void courseSignUp(SignUpDto signUpDto) {
		String mobile = signUpDto.getMobile();

		String userId = signUpDto.getUserId();
		
		// 有验证码和用户id,说明手机号不存在，需要给用户绑定手机号
		if (!StringUtils.isBlank(signUpDto.getCode()) && !StringUtils.isBlank(userId)) {
			validateRedisCode(mobile, signUpDto.getCode());
			User user = userService.loadUserInfoByMobile(mobile);
			if(user != null) {
				throw new BusinessException(ResultEnum.MOBILE_EXIST);
			}
			User user2 = new User();
			user2.setId(userId);
			user2.setMobile(mobile);
			user2.setNickname(signUpDto.getUsername());
			userService.updateUserInfo(user2);
		}
		// 注册用户,注册完成以后并登陆
		else if (!StringUtils.isBlank(signUpDto.getCode()) && StringUtils.isBlank(userId)) {
			validateRedisCode(mobile, signUpDto.getCode());
			User user = userService.loadUserInfoByMobile(mobile);
			if(user != null) {
				throw new BusinessException(ResultEnum.MOBILE_EXIST);
			}
			User user2 = new User();
			userId = IdUtils.getId();
			user2.setId(userId);
			user2.setMobile(mobile);
			user2.setUsername(mobile);
			user2.setNickname(signUpDto.getUsername());
			User insert = userService.insert(user2);
			
			SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(insert,insert.getAuthorities());
			// 给用户登陆
			SecurityContextHolder.getContext().setAuthentication((Authentication) smsCodeAuthenticationToken);
			redisUtils.set("login_token:" + mobile, JSONObject.toJSONString(insert));
		}
		//如果验证码不存在，但是用户名存在，说明已经登陆了,更新下用户名
		else if (StringUtils.isBlank(signUpDto.getCode()) && !StringUtils.isBlank(userId)) {
			User user2 = new User();
			user2.setId(userId);
			user2.setNickname(signUpDto.getUsername());
			userService.updateUserInfo(user2);
		}else {
			throw new BusinessException(ResultEnum.PARAM_ERROR);
		}
		courseMapper.courseSignUp(signUpDto.getId(), userId);

	}

	/**
	 * 校验验证码
	 * 
	 * @param mobile
	 * @param code
	 */
	private void validateRedisCode(String mobile, String code) {
		String string = redisUtils.get("code:sms:" + mobile);
		if (StringUtils.isNoneBlank(string)) {
			ValidateCode validateCode = JSONObject.parseObject(string, ValidateCode.class);
			LocalDateTime localDateTime = LocalDateTime.now();
			LocalDateTime minusSeconds = validateCode.getExpireTime().minusSeconds(Constants.CODE_IN_REDIS_TIME)
					.plusSeconds(Constants.CODE_RESEND);

			if (minusSeconds.compareTo(localDateTime) > 0) {
				throw new BusinessException(ResultEnum.CODE_EXIST);
			}

			if (!code.equals(validateCode.getCode())) {
				throw new BusinessException(ResultEnum.VALIDATECODE_ERROR);
			}

		} else {
			throw new BusinessException(ResultEnum.CODE_NOT_EXIST);
		}
	}

}
