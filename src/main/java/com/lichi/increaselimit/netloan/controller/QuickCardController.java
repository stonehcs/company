package com.lichi.increaselimit.netloan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.QuickCard;
import com.lichi.increaselimit.netloan.service.QuickCardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 网贷
 * @author majie
 *
 */	
@RestController
@RequestMapping("/quick-card")
@Api(description = "快卡")
@Slf4j
public class QuickCardController{

	@Autowired
	private QuickCardService quickCardService;
	
	@ApiOperation("查看快卡信息")
	@GetMapping
	public ResultVo<List<QuickCard>> getAll(@RequestParam(required=false) Integer type){
		
		log.info("查询快卡网贷信息");
		List<QuickCard> list = quickCardService.selectAll(type);
		return ResultVoUtil.success(list);
		
	}
}
