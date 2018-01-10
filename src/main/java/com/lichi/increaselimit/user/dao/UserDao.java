package com.lichi.increaselimit.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.course.entity.CourseVo;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserRank;
import com.lichi.increaselimit.user.entity.VipLevel;

/**
 * userdao
 * 
 * @author majie
 *
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

	/**
	 * 获取用户信息
	 * 
	 * @param mobile
	 * @return
	 */
	@Select("select * from t_user where mobile=#{mobile}")
	User loadUserInfoByMobile(String mobile);

	@Select("select b.*,d.teachername,d.img_url,c.status from t_user a,t_course b,t_user_course c,t_teacher d "
			+ "where a.id=c.user_id "
			+ "and b.id = c.course_id "
			+ "and d.id = b.teacher_id "
			+ "and c.user_id = #{id} and c.status = #{status}")
	List<CourseVo> selectUserCourse(@Param(value = "id") String id, @Param(value = "status") Integer status);

	/**
	 * 获取积分排名
	 * @param id
	 * @return
	 */
	@Select("select * from (select @rownum:=@rownum+1  rownum , a.id , a.invitation,a.points from t_user a,(SELECT @rownum:=0) r  "
			+ "order by a.invitation desc,create_time desc ) t where t.id = #{id}")
	UserRank getRank(String id);
	
	/**
	 * 获取指定排名信息
	 * @param id
	 * @return
	 */
	@Select("select * from (select @rownum:=@rownum+1  rownum , a.id , a.invitation,a.points from t_user a,(SELECT @rownum:=0) r  "
			+ "order by a.invitation desc,create_time desc ) t where t.rownum = #{rownum}")
	UserRank getRankByRow(Integer rownum);

	@Select("select * from t_vip_level where id = #{level}")
	VipLevel selectLevelInfo(Integer level);

	
	/**
	 * 更新上级用户邀请人数
	 * @param pid
	 */
	@Update("update t_user set invitation = invitation + 1 where id = #{pid}")
	void updatePidInvitaion(String pid);

	@Select("select * from (select @rownum:=@rownum+1  rownum , a.* from t_user a,(SELECT @rownum:=0) r  "
			+ "order by a.invitation desc,create_time desc ) t")
	List<User> selectAllRank();
}
