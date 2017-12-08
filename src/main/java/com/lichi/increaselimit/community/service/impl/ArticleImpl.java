package com.lichi.increaselimit.community.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.community.dao.ArticleMapper;
import com.lichi.increaselimit.community.dao.CircleMapper;
import com.lichi.increaselimit.community.entity.Article;
import com.lichi.increaselimit.community.entity.Circle;
import com.lichi.increaselimit.community.service.ArticleService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author by majie on 2017/11/15.
 */
@Service
public class ArticleImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleDao;
	@Autowired
	private CircleMapper circleDao;

	@Override
	public Article get(Integer id) {

		return articleDao.selectByPrimaryKey(id);
	}

	@Override
	public PageInfo<Article> getByPage(Integer page, Integer size, Integer circleId) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("create_time desc");
		Example example = new Example(Article.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("circleId",circleId);
		List<Article> list = articleDao.selectByExample(example);
		PageInfo<Article> pageInfo = new PageInfo<Article>(list);
		return pageInfo;
	}

	/**
	 * 先查询圈子是否存在,不然不能发帖
	 */
	@Override
	public void add(Article article) {
		Circle circle = circleDao.selectByPrimaryKey(article.getCircleId());
		if (circle == null) {
			throw new BusinessException(ResultEnum.CIRCLE_NO_EXIST);
		}
		article.setCreateTime(new Date());
		article.setUpdateTime(new Date());
		articleDao.insertSelective(article);
	}

	@Override
	public void update(Article article) {
		article.setUpdateTime(new Date());
		articleDao.updateByPrimaryKeySelective(article);
	}

	@Override
	public void delete(Integer id) {
		articleDao.deleteByPrimaryKey(id);
	}


	@Override
	public PageInfo<Article> getHotByPage(Integer page, Integer size) {
		PageHelper.startPage(page, size);
		PageHelper.orderBy("sort desc,create_time desc");
		List<Article> list = articleDao.selectAll();
		PageInfo<Article> pageInfo = new PageInfo<Article>(list);
		return pageInfo;
	}
}
