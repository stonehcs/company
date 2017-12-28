package com.lichi.increaselimit.user.service;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.user.entity.Version;

public interface VersionService {

	/**
	 * 分页查询所有版本
	 * @param page
	 * @param size
	 * @return
	 */
	PageInfo<Version> selectAll(Integer page, Integer size);

	/**
	 * 根据id查询版本
	 * @param id
	 * @return
	 */
	Version selectOne(Integer id);

	/**
	 * 新增一个版本
	 * @param dept
	 */
	void insertOne(Version dept);

	/**
	 * 删除版本
	 * @param id
	 */
	void deleteOne(Integer id);

	/**
	 * 更新版本信息
	 * @param dept
	 */
	void update(Version dept);

	/**
	 * 查询所有
	 * @return
	 */
	Version selectNew();

}
