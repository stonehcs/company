package com.lichi.increaselimit.community.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 帖子
 * @author by majie on 2017/11/15.
 */
@Data
@Table(name = "t_article")
public class Article implements Serializable{

	private static final long serialVersionUID = -1997630198932216787L;

	@Id
    private Integer id;

    private String title;

    private String content;
    
//    @OrderBy("desc") //可以直接排序
    private Date createTime;

    private Date updateTime;

    @Column(updatable=false)
    private String createUserId;

    @Column(updatable=false)
    private Integer circleId;
    
    private Integer sort;
    
    private String summary;
}
