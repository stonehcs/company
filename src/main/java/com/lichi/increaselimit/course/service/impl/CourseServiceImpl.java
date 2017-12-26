package com.lichi.increaselimit.course.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.lichi.increaselimit.security.validate.code.ValidateCode;
import com.lichi.increaselimit.user.entity.CourseCount;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

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
		list.stream().forEach(e -> {
			getPersons(e);
		});
		PageInfo<CourseVo> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<CourseVo> getCourseList(Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("start_time asc");
		List<CourseVo> list = courseMapper.selectListIndex();
		list.stream().forEach(e -> {
			getPersons(e);
		});
		PageInfo<CourseVo> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public CourseVo getCourse(Integer id) {
		CourseVo courseVo = courseMapper.selectCourseDetails(id);
		getPersons(courseVo);
		return courseVo;
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
	public PageInfo<CourseVo> seleteByLike(Integer page, Integer size, String name) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("start_time asc");
		List<CourseVo> list = courseMapper.selectByLike(name);
		list.stream().forEach(e -> {
			getPersons(e);
		});
		PageInfo<CourseVo> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	
	/**
	 * 获取报名人数
	 * @param vo
	 */
	private void getPersons(CourseVo vo) {
		Integer signpersons = courseMapper.getCount(vo.getId(), 0);
		vo.setSignUpPerson(signpersons);
		Integer paypersons = courseMapper.getCount(vo.getId(), 1);
		vo.setPayPerson(paypersons);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String courseSignUp(SignUpDto signUpDto) {
		String mobile = signUpDto.getMobile();

		String userId = signUpDto.getUserId();
		
		String token = null;
		
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
			token = IdUtils.getUUID();
			// 给用户登陆
			redisUtils.set("login_token:" + token, JSONObject.toJSONString(insert));
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
		Integer status = courseMapper.selectStatus(signUpDto.getId(), userId);
		if(null == status) {
			courseMapper.courseSignUp(signUpDto.getId(), userId);
			redisUtils.del(Constants.MOBILE_REDIS_KEY + mobile);
		}else if(0 == status) {
			throw new BusinessException(ResultEnum.COURSE_HAS_SIGNUP);
		}else if(1 == status) {
			throw new BusinessException(ResultEnum.COURSE_HAS_PAY);
		}
		
		return token;

	}

	/**
	 * 校验验证码
	 * 
	 * @param mobile
	 * @param code
	 */
	private void validateRedisCode(String mobile, String code) {
		String string = redisUtils.get(Constants.MOBILE_REDIS_KEY + mobile);
		if (StringUtils.isNoneBlank(string)) {
			ValidateCode validateCode = JSONObject.parseObject(string, ValidateCode.class);
			
			if (!code.equals(validateCode.getCode())) {
				throw new BusinessException(ResultEnum.VALIDATECODE_ERROR);
			}

		} else {
			throw new BusinessException(ResultEnum.CODE_NOT_EXIST);
		}
	}

	@Override
	public CourseCount getMyCourse(String id) {
		Integer signUp = courseMapper.getMyCourse(id,0);
		Integer pay = courseMapper.getMyCourse(id,1);
		return new CourseCount(pay, signUp);
	}


}
