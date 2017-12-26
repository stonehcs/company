package com.lichi.increaselimit.netloan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.NetLoad;
import com.lichi.increaselimit.netloan.service.NetLoadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 网贷
 * @author majie
 *
 */	
@RestController
@RequestMapping("/netload")
@Api(description = "网贷")
@Slf4j
public class NetLoadController{

	@Autowired
	private NetLoadService netLoadService;
	
	@ApiOperation("查看网贷信息")
	@GetMapping
	public ResultVo<List<NetLoad>> getAll(){
		
		log.info("查询所有网贷信息");
		List<NetLoad> list = netLoadService.selectAll();
		return ResultVoUtil.success(list);
		
	}
}
