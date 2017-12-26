package com.lichi.increaselimit.netloan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.NetLoan;
import com.lichi.increaselimit.netloan.service.NetLoanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 网贷
 * @author majie
 *
 */	
@RestController
@RequestMapping("/netloan")
@Api(description = "网贷")
@Slf4j
public class NetLoanController{

	@Autowired
	private NetLoanService netLoadService;
	
	@ApiOperation("查看网贷信息")
	@GetMapping
	public ResultVo<List<NetLoan>> getAll(){
		
		log.info("查询所有网贷信息");
		List<NetLoan> list = netLoadService.selectAll();
		return ResultVoUtil.success(list);
		
	}
}
