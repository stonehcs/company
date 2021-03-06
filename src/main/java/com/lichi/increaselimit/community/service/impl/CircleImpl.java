package com.lichi.increaselimit.community.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.community.dao.ArticleDao;
import com.lichi.increaselimit.community.dao.CircleDao;
import com.lichi.increaselimit.community.entity.Article;
import com.lichi.increaselimit.community.entity.Circle;
import com.lichi.increaselimit.community.entity.CircleVo;
import com.lichi.increaselimit.community.service.CircleService;

import tk.mybatis.mapper.entity.Example;

/**
 * @author by majie on 2017/11/15.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CircleImpl implements CircleService {

    @Autowired
    private CircleDao circleDao;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public CircleVo get(Integer id) {
        return circleDao.selectByCircleId(id);
    }

    @Override
    public PageInfo<CircleVo> getByPage(Integer page, Integer size) {
    	PageHelper.startPage(page, size);
    	PageHelper.orderBy("sort1 desc,create_time desc");
        return getArticleCount();
    }
    
    @Override
    public PageInfo<CircleVo> getHostByPage(Integer page, Integer size) {
    	PageHelper.startPage(page, size);
    	PageHelper.orderBy("sort2 desc,create_time desc");
    	return getArticleCount();
    }
    
    /**
     * 添加圈子时候名字不能重复
     */
    @Override
    public void add(Circle circle) {
    	getByName(circle);
        circle.setCreateTime(new Date());
        circle.setUpdateTime(new Date());
        circleDao.insertSelective(circle);
    }

    /**
     * 更新时候圈子名称不能重复
     */
    @Override
    public void update(Circle circle) {
    	if(circle.getName() != null) {
    		getByName(circle);
    	}
        circle.setUpdateTime(new Date());
        circleDao.updateByPrimaryKeySelective(circle);
    }
    
    /**
     * 删除的时候先查询是否有帖子
     */
    @Override
    public void delete(Integer id) {
    	Integer resutl = queryArticleCount(id);
        if(resutl > 0){
            throw new BusinessException(ResultEnum.ARTICLE_NO_EMPTY);
        }
        circleDao.deleteByPrimaryKey(id);
    }

    /**
     * 查询对应圈子id的帖子数量
     * @param id
     * @return
     */
	private Integer queryArticleCount(Integer id) {
		Example example = new Example(Article.class);
    	example.createCriteria().andEqualTo("circleId",id);
        Integer resutl = articleDao.selectCountByExample(example);
		return resutl;
	}
    
    /**
     * 查询对应圈子下的帖子数量
     * @param list
     * @return
     */
	private PageInfo<CircleVo> getArticleCount() {
		List<CircleVo> list = circleDao.selectList();
		if(list == null) {
    		return null;
    	}
    	for (CircleVo circle : list) {
    		Integer count = queryArticleCount(circle.getId());
    		circle.setCount(count);
    	}
    	PageInfo<CircleVo> pageInfo = new PageInfo<CircleVo>(list);
    	return pageInfo;
	}
	
	/**
	 * 根据名字查询
	 * @param circle
	 */
	private void getByName(Circle circle) {
		Example example = new Example(Circle.class);
    	example.createCriteria().andEqualTo("name",circle.getName());
		List<Circle> list = circleDao.selectByExample(example);
		if(!list.isEmpty()) {
			if(list.get(0).getId() != circle.getId()) {
				throw new BusinessException(ResultEnum.CIRCLE_HAS_EXIST);
			}
		}
	}

	@Override
	public PageInfo<CircleVo> seleteByLike(Integer page, Integer size , String name) {
    	PageHelper.startPage(page, size);
    	PageHelper.orderBy("sort2 desc,create_time desc");
    	List<CircleVo> list = circleDao.selectListLike(name);
    	PageInfo<CircleVo> pageInfo = new PageInfo<CircleVo>(list);
		return pageInfo;
	}
}
