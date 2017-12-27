package com.lichi.increaselimit.user.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.user.entity.SysMessage;

public interface SysMessageService {

	/**
	 * 分页查询所有系统消息
	 * @param page
	 * @param size
	 * @param userId 
	 * @return
	 */
	PageInfo<SysMessage> selectAll(Integer page, Integer size, String userId);

	/**
	 * 根据id查询系统消息
	 * @param id
	 * @return
	 */
	SysMessage selectOne(Integer id);

	/**
	 * 查询所有
	 * @param userId 
	 * @return
	 */
	List<SysMessage> selectList(String userId);

}
