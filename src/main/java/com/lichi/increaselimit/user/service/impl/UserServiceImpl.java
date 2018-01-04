package com.lichi.increaselimit.user.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.HuanXinUtils;
import com.lichi.increaselimit.common.utils.IdUtils;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.course.service.CourseService;
import com.lichi.increaselimit.user.dao.SocialUserDao;
import com.lichi.increaselimit.user.dao.UserDao;
import com.lichi.increaselimit.user.entity.CourseCount;
import com.lichi.increaselimit.user.entity.SocialUserInfo;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserRank;
import com.lichi.increaselimit.user.entity.VipLevel;
import com.lichi.increaselimit.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userMapper;
	@Autowired
	private SocialUserDao socialUserMapper;
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private RestTemplate restTemplate;
 
	@Override
	public User loadUserInfoByMobile(String mobile) {
		User user = userMapper.loadUserInfoByMobile(mobile);
		return user;
	}

	@Override
	public User loadUserInfoByUserId(String userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		return user;
	}

	/**
	 * 还要注册环信用户
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insertSocialUser(SocialUserInfo socialUserInfo) throws BusinessException{
		User user = new User();
		String userId = IdUtils.getId();
		log.info("生成的用户id:{}",userId);
		socialUserInfo.setUserId(userId);
		// 第三方表
		socialUserMapper.insertUserConnection(socialUserInfo);
		user.setId(userId);
		if(StringUtils.isBlank(socialUserInfo.getImageUrl())) {
			user.setHeadImg("http://ozlfwi1zj.bkt.clouddn.com/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.jpg?imageView2/1/w/600/h/400/q/75|watermark/2/text/6YeN5bqG6aqK6amw5paH5YyW/font/5b6u6L2v6ZuF6buR/fontsize/480/fill/I0ZGRkZGRg==/dissolve/100/gravity/SouthEast/dx/10/dy/10|imageslim");
		}else {
			user.setHeadImg(socialUserInfo.getImageUrl());
		}
		user.setNickname(socialUserInfo.getDisplayName());
		user.setVipLevel(1);
		// 用户表
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userMapper.insertSelective(user);
		//注册环信用户
//		try {
			HuanXinUtils.registerUser(userId, restTemplate);
//		} catch (Exception e) {
//			throw new BusinessException(ResultEnum.REGISTER_ERROR);
//		}
		return user;
	}

//	@Override
//	public User loadUserInfoByUsername(String username) {
//		List<User> list = selectByUsername(username);
//		return CollectionUtils.isEmpty(list) ? null : list.get(0);
//	}

	/**
	 * 通过用户名查找用户信息
	 * 
	 * @param username
	 * @return
	 */
	private List<User> selectByUsername(String username) {
		Example example = new Example(User.class);
		example.createCriteria().andEqualTo("username", username);
		List<User> list = userMapper.selectByExample(example);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insertMobileUser(String mobile) throws BusinessException{
		String userId = IdUtils.getId();
		log.info("生成的用户id:{}",userId);
		User user = new User();
		user.setId(userId);
		user.setNickname(mobile);
		user.setMobile(mobile);
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setVipLevel(1);
		user.setHeadImg("http://ozlfwi1zj.bkt.clouddn.com/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.jpg?imageView2/1/w/600/h/400/q/75|watermark/2/text/6YeN5bqG6aqK6amw5paH5YyW/font/5b6u6L2v6ZuF6buR/fontsize/480/fill/I0ZGRkZGRg==/dissolve/100/gravity/SouthEast/dx/10/dy/10|imageslim");
		userMapper.insertSelective(user);
//		try {
			HuanXinUtils.registerUser(userId, restTemplate);
//		} catch (Exception e) {
//			throw new BusinessException(ResultEnum.REGISTER_ERROR);
//		}
		return user;
	}

	@Override
	public PageInfo<User> selectBank(Integer page, Integer size) {
		PageHelper.startPage(page,size);
		PageHelper.orderBy("invitation desc,create_time desc");
		List<User> list = userMapper.selectAll();
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<CourseVo> selectCourse(Integer page, Integer size, String id, Integer status) {
		PageHelper.startPage(page,size);
		List<CourseVo> list = userMapper.selectUserCourse(id,status);
		PageInfo<CourseVo> pageInfo = new PageInfo<CourseVo>(list);
		return pageInfo;
	}
	
	@Override
	public CourseCount getMyCourse(String id) {
		CourseCount courseCount = courseService.getMyCourse(id);
		return courseCount;
	}

	@Override
	public void updateUserInfo(User user) {
		user.setUpdateTime(new Date());
		User user2 = userMapper.loadUserInfoByMobile(user.getMobile());
		if(user2 != null && !user2.getId().equals(user.getId())) {
			throw new BusinessException(ResultEnum.MOBILE_EXIST);
		}else {
			userMapper.updateByPrimaryKeySelective(user);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User insert(User user) {
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		userMapper.insertSelective(user);
		try {
			HuanXinUtils.registerUser(user.getId(), restTemplate);
		} catch (Exception e) {
			throw new BusinessException(ResultEnum.REGISTER_ERROR);
		}
		return user;
	}

	@Override
	public UserRank getUserRank(String id) {
		UserRank userRank = userMapper.getRank(id);
		if(userRank.getRownum() != 1) {
			UserRank rankByRow = userMapper.getRankByRow(userRank.getRownum() + 1);
			if(rankByRow != null) {
				userRank.setDiff(userRank.getInvitation() - rankByRow.getInvitation());
			}else {
				userRank.setDiff(0);
			}
		}else {
			userRank.setDiff(0);
		}
		return userRank;
	}

	@Override
	public VipLevel getLevel(Integer level) {
		return userMapper.selectLevelInfo(level);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void registerHuanxinAndUpdatePid(String userId,String pid) {
		User user = new User();
		user.setUpdateTime(new Date());
		user.setId(userId);
		userMapper.updateByPrimaryKeySelective(user);
		
		userMapper.updatePidInvitaion(pid);
		try {
			HuanXinUtils.registerUser(userId, restTemplate);
		} catch (Exception e) {
			throw new BusinessException(ResultEnum.REGISTER_ERROR);
		}
		
	}

	@Override
	public void deleteByMobile(String mobile) {
		Example example = new Example(User.class);
		example.createCriteria().andEqualTo("mobile",mobile);
		userMapper.deleteByExample(example);
	}

	@Override
	public void deleteByPrimary(String id) {
		userMapper.deleteByPrimaryKey(id);
	}


}
