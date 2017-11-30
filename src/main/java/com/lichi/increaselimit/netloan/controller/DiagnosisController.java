package com.lichi.increaselimit.netloan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.service.DiagnosisDicService;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 诊断
 * @author majie
 *
 */
@RestController
@RequestMapping("/diagnosis")
@Api(description = "诊断")
public class DiagnosisController {
	
	@Autowired
	private DiagnosisDicService diagnosisDicService;
	
	@Autowired
	private DiagnosisResultService diagnosisResultService;
	
	@ApiOperation("诊断并获取诊断结果")
	@GetMapping
	public ResultVo<DiagnosisResultList> diagnosis(@RequestParam String bankname,@RequestParam int money) {
		DiagnosisResultList result = diagnosisDicService.getResult(bankname, money);
		return ResultVoUtil.success(result);
	}
	
	@ApiOperation("查询诊断结果")
	@GetMapping("/get")
	public ResultVo<DiagnosisResultList> getDiagnosisResult(@RequestParam String userId,@RequestParam String bankname) {
		DiagnosisResultList result = diagnosisResultService.getResult(userId, bankname);
		return ResultVoUtil.success(result);
	}
	
	@ApiOperation("更新消费金额")
	@PutMapping
	public ResultVo<DiagnosisResultList> update(@RequestParam Integer id,@RequestParam Double money) {
		DiagnosisResultList result = diagnosisResultService.update(id, money);
		return ResultVoUtil.success(result);
	}
}
