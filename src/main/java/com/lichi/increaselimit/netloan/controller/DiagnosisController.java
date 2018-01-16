package com.lichi.increaselimit.netloan.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.service.DiagnosisDicService;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;
import com.lichi.increaselimit.third.entity.CreditBill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 诊断
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/diagnosis")
@Api(description = "诊断")
@Slf4j
public class DiagnosisController {

	@Autowired
	private DiagnosisDicService diagnosisDicService;

	@Autowired
	private DiagnosisResultService diagnosisResultService;


	@ApiOperation("一键提额")
	@GetMapping
	public ResultVo<DiagnosisResultList> diagnosis(CreditBill creditBill){

		log.info("一键提额,用户id:{}",creditBill.getUserId());
		
		String creditAmt = creditBill.getCreditAmt();
		creditAmt = creditAmt.replaceAll(",", "");
		creditAmt = creditAmt.replaceAll("-", "");
		creditAmt = StringUtils.isBlank(creditAmt) ? "10000" : creditAmt;
		double money = Double.parseDouble(creditAmt);

		DiagnosisResultList result = diagnosisDicService.getResult(creditBill.getIssueBank(), money,
				creditBill.getLast4digit(), creditBill.getUserId(),creditBill.getHolderName());

		String format = String.format(TEXT, creditBill.getHolderName(),creditBill.getLast4digit(),"100%","150%",money * 1,money*1.5);
		result.setResult(format);

		return ResultVoUtil.success(result);
	}

	@ApiOperation("查询诊断结果")
	@GetMapping("/get")
	public ResultVo<DiagnosisResultList> getDiagnosisResult(
			@ApiParam(value = "用户id", required = true) @RequestParam(required = true) String userId,
			@ApiParam(value = "银行卡名字", required = true) @RequestParam(required = true) String bankname,
			@ApiParam(value = "卡号后四位", required = true) @RequestParam(required = true) String last4digit) {
		log.info("查询诊断结果,用户id:{}",userId);
		
    	if(StringUtils.isBlank(userId)) {
    		throw new BusinessException(ResultEnum.USERID_NOT_CHOICE);
    	}
    	if(bankname == null) {
    		throw new BusinessException(ResultEnum.BANKNAME_NOT_CHOICE);
    	}
    	if(last4digit == null) {
    		throw new BusinessException(ResultEnum.LAST4_NOT_CHOICE);
    	}
		DiagnosisResultList result = diagnosisResultService.getResult(userId, bankname, last4digit);
		return ResultVoUtil.success(result);
	}
	
	@ApiOperation("雷达文案")
	@GetMapping("/radar")
	public ResultVo<String[]> get() {
		log.info("获取雷达文案");
		String[] strings = RADAR.split("\\r\\n");
		return ResultVoUtil.success(strings);
	}

	@ApiOperation("更新消费金额")
	@PostMapping("/update")
	public Object update(@RequestParam Integer id, @RequestParam Double money) {
		log.info("更新诊断结果消费金额,诊断结果的id:{}",id);
		diagnosisResultService.update(id, money);
		return ResultVoUtil.success();
	}
	

	private final static String TEXT = "尊敬的%s，尾号%s客户，根据平台大数据与您自身的消费账单分析，请在“我的->消费任务”中，按照每日消费任务，提高诊断评分。可在1-3个月"
			+ "内提升信用卡额度%s-%s，预计可达到金额:%s-%s。";
	
	private final static String RADAR = "一键诊断程序启动中...\r\n" + 
			"银行卡基本信息确认完成。\r\n" + 
			"最近3个月消费账单分析完成。\r\n" + 
			"刷卡消费类型分析完成。\r\n" + 
			"诊断模型分析完成。\r\n" + 
			"一键诊断完成。";
	
	
	public static void main(String[] args) {
		String format = String.format(TEXT, "xx","xx","100%","150%",0.5 * 1,0.5*1.5);
		System.out.println(format);
	}
}
