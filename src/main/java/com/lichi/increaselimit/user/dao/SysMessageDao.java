package com.lichi.increaselimit.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.SysMessage;

/**
 * SysMessageDao
 * @author majie
 *
 */
@Mapper
public interface SysMessageDao extends BaseMapper<SysMessage>{

	@Select("SELECT * from t_sys_message where level = #{level} UNION SELECT * from t_sys_message where user_id = #{userId}")
	List<SysMessage> selectAllMessage(@Param(value = "userId") String userId,@Param(value = "level") Integer level);
	
}
