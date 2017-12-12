package com.lichi.increaselimit.course.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.course.entity.Video;

/**
 * CourseService
 * @author majie
 *
 */
public interface VideoService {

	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	Video getVideo(Integer id);

	/**
	 * 添加课程
	 * @param video
	 */
	void addVideo(Video video);

	/**
	 * 删除视频
	 * @param id
	 */
	void deleteVideo(Integer id);

	/**
	 * 更新
	 * @param video
	 */
	void updateVideo(Video video);

	/**
	 * 分页
	 * @param page
	 * @param size
	 * @return
	 */
	PageInfo<Video> getVideoList(Integer page, Integer size);



}
