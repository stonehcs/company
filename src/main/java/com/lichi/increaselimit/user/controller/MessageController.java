package com.lichi.increaselimit.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.user.entity.SysMessage;
import com.lichi.increaselimit.user.service.SysMessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api(description = "消息信息")
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

	@Autowired
	private SysMessageService messageService;

	@GetMapping("/page")
	@ApiOperation("分页查询所有消息")
	public ResultVo<PageInfo<SysMessage>> getAll(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size,
			@RequestParam(required = true) String userId) {
		log.info("分页查询所有消息,用户id:{}",userId);
    	if(userId == null) {
    		throw new BusinessException(ResultEnum.USERID_NOT_CHOICE);
    	}
		PageInfo<SysMessage> list = messageService.selectAll(page, size,userId);
		return ResultVoUtil.success(list);
	}
	
	@GetMapping("/list")
	@ApiOperation("查询所有消息")
	public ResultVo<List<SysMessage>> getAll(@RequestParam(required = true) String userId) {
		log.info("查询所有消息,用户id:{}",userId);
    	if(userId == null) {
    		throw new BusinessException(ResultEnum.USERID_NOT_CHOICE);
    	}
		List<SysMessage> list = messageService.selectList(userId);
		return ResultVoUtil.success(list);
	}


	@GetMapping("/{id}")
	@ApiOperation("根据id查询消息信息")
	public ResultVo<SysMessage> get(@PathVariable Integer id) {
		log.info("查询消息信息,消息id:{}",id);
		SysMessage list = messageService.selectOne(id);
		return ResultVoUtil.success(list);
	}
}
