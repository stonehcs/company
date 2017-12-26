package com.lichi.increaselimit.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.community.entity.Circle;
import com.lichi.increaselimit.community.entity.CircleVo;

/**
 * @author by majie on 2017/11/15.
 */
@Mapper
public interface CircleDao extends BaseMapper<Circle>{

	/**
	 * 根据圈子id查询对应圈子信息
	 * @param id
	 * @return
	 */
	@Select("select a.*,b.nickname,b.id as createUserId from t_circle a, t_sys_user b where a.create_user_id = b.id and a.id = #{id}")
	CircleVo selectByCircleId(Integer id);

	/**
	 * 查询所有圈子信息
	 * @return
	 */
	@Select("select a.*,b.nickname,b.id as createUserId from t_circle a, t_sys_user b where a.create_user_id = b.id")
	List<CircleVo> selectList();
	
	/**
	 * 模糊查询
	 * @return
	 */
	@Select("SELECT a.*, b.nickname, b.id AS createUserId FROM t_circle a left JOIN t_sys_user b " + 
			"ON a.create_user_id = b.id where a.name LIKE concat('%', #{name}, '%')" + 
			"or b.nickname LIKE concat('%', #{name}, '%')")
	List<CircleVo> selectListLike(String name);
	
}
