package com.lichi.increaselimit.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.lichi.increaselimit.common.mapper.BaseMapper;
import com.lichi.increaselimit.community.entity.Article;
import com.lichi.increaselimit.community.entity.ArticleVo;
import com.lichi.increaselimit.community.entity.CircleArticle;


/**
 * @author by majie on 2017/11/15.
 */
@Mapper
public interface ArticleDao extends BaseMapper<Article>{

	/**
	 * 根据圈子id查询帖子列表
	 * @param circleId
	 * @return
	 */
	@Select("select a.*,b.nickname,b.id as createUserId from t_article a ,"
			+ " t_sys_user b where a.create_user_id = b.id and a.circle_id = #{circleId} ")
	List<ArticleVo> selectByCircleId(Integer circleId);

	@Select("select a.*,b.nickname,b.id as createUserId, c.name from t_article a ,"
			+ " t_sys_user b ,t_circle c where a.create_user_id = b.id and c.id = a.circle_id")
	List<ArticleVo> selectHot();

	@Select("select a.*,b.name from t_article a ,"
			+ " t_circle b where a.circle_id = b.id")
	List<CircleArticle> selectIndex();
	

}
