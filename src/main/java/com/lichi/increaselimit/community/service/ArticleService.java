package com.lichi.increaselimit.community.service;

import com.lichi.increaselimit.community.entity.Article;
import org.springframework.data.domain.Page;

/**
 * @author by majie on 2017/11/15.
 */
public interface ArticleService {

    /**
     * 根据帖子id查询帖子
     * @param id
     * @return
     */
    Article get(Integer id);

    /**
     * 分页查询
     * @param page
     * @param size
     * @param circleId 
     * @return
     */
    Page<Article> getByPage(Integer page, Integer size, Integer circleId);

    /**
     * 发帖子
     * @param article
     */
    void add(Article article);

    /**
     * 更新帖子
     * @param article
     */
    void update(Article article);

    /**
     * 根据id删除帖子
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询该articleId下所有帖子的数目
     * @param articleId
     * @return
     */
    Integer getCountByCircleId(Integer articleId);
}
