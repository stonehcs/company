package com.lichi.increaselimit.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.Video;

@Mapper
public interface VideoDao extends BaseMapper<Video>{
	
	@Select("select a.* from t_video a where (a.description LIKE concat('%', #{description}, '%') )")
	List<Video> selectByLike(String description);
	
}
