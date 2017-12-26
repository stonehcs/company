package com.lichi.increaselimit.user.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.user.entity.SocialUserInfo;

/**
 * userdao
 * @author majie
 *
 */
@Mapper
public interface SocialUserDao extends BaseMapper<SocialUserInfo>{
	
	/**
	 * 获取用户信息
	 * @param username
	 * @return
	 */
	@Select("select * from t_UserConnection where providerId=#{providerId} and providerUserId = #{openid}")
	String getUserIdByProviderIdAndOpenid(@Param(value = "providerId") String providerId,@Param(value = "openid")String openid);
	
	@Insert("insert into t_UserConnection ( userId,providerId,providerUserId,rank,displayName,profileUrl,"
			+ "imageUrl,accessToken,secret,refreshToken,expireTime) "
			+ "VALUES(#{userId},#{providerId},#{providerUserId},#{rank},#{displayName},#{profileUrl},"
			+ "#{imageUrl},#{accessToken},#{secret},#{refreshToken},#{expireTime})")
	void insertUserConnection(SocialUserInfo socialUserInfo);
}
