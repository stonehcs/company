package com.lichi.increaselimit.community.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.community.entity.Circle;
import com.lichi.increaselimit.community.entity.CircleVo;

/**
 * @author by majie on 2017/11/15.
 */
public interface CircleService {

    /**
     * 根据id查询圈子
     * @param id
     * @return
     */
    CircleVo get(Integer id);

    /**
     * 查询所有圈子
     * @param page
     * @param size
     * @return
     */
    PageInfo<CircleVo> getByPage(Integer page, Integer size);

    /**
     * 新增圈子
     * @param circle
     */
    void add(Circle circle);

    /**
     * 更新圈子
     * @param circle
     */
    void update(Circle circle);

    /**
     * 根据id删除圈子
     * @param id
     */
    void delete(Integer id);
    
    /**
     * 查询热门圈子
     * @param page
     * @param size
     * @return
     */
	PageInfo<CircleVo> getHostByPage(Integer page, Integer size);

}
