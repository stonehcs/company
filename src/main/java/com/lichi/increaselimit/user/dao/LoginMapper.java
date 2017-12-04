package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.LoginUser;

/**
 * logindao
 * @author majie
 *
 */
@Mapper
public interface LoginMapper extends BaseMapper<LoginUser>{
	
}
