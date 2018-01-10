package com.lichi.increaselimit.third.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.third.entity.EmailDic;
import com.lichi.increaselimit.third.service.EmailDicService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "邮箱字典表")
public class EmailDicController {
	
	@Autowired
	private EmailDicService emailDicService;
	
	@GetMapping("/email/list")
	@ApiOperation("获取所有的邮箱列表")
	public ResultVo<List<EmailDic>> getAllEmailDic() {
		List<EmailDic> allEmailDic = emailDicService.getAllEmailDic();
		return ResultVoUtil.success(allEmailDic);
	}
	
}
