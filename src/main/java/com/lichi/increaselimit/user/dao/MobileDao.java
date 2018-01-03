package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.Mobile;

@Mapper
public interface MobileDao extends BaseMapper<Mobile>{

	@Select("select * from mobile where mobile = #{mobile}")
	Mobile selectByMobile(String mobile);
}
