package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.User;

/**
 * userdao
 * @author majie
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{
	
	/**
	 * 获取用户信息
	 * @param username
	 * @return
	 */
	@Select("select * from t_user where mobile=#{mobile}")
	User loadUserInfoByMobile(String mobile);
}
