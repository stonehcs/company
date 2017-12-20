package com.lichi.increaselimit.netloan.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.netloan.entity.DiagnosisResultList;
import com.lichi.increaselimit.netloan.service.DiagnosisDicService;
import com.lichi.increaselimit.netloan.service.DiagnosisResultService;
import com.lichi.increaselimit.third.entity.CreditBill;
import com.lichi.increaselimit.third.service.CreditBillService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 诊断
 * 
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

	@Autowired
	private CreditBillService creditBillService;

	@ApiOperation("获取当前用户所用信用卡")
	@GetMapping("/bill/{userId}")
	public ResultVo<List<CreditBill>> diagnosis(@PathVariable String userId) {
		// List<DiagnosisResultList> resultlist = new ArrayList<DiagnosisResultList>();
		List<CreditBill> list = creditBillService.selectByUserId(userId);
		// list.forEach(e -> {
		// String creditAmt = e.getCreditAmt();
		// creditAmt = StringUtils.isBlank(creditAmt) ? "10000" : creditAmt;
		// creditAmt = creditAmt.replaceAll(",", "");
		// double money = Double.parseDouble(creditAmt);
		// DiagnosisResultList result = diagnosisDicService.getResult(e.getIssueBank(),
		// money,e.getLast4digit(),userId);
		// resultlist.add(result);
		// });
		return ResultVoUtil.success(list);
	}

	@ApiOperation("一键提额")
	@GetMapping
	public ResultVo<DiagnosisResultList> diagnosis(CreditBill creditBill) throws IOException {

		String creditAmt = creditBill.getCreditAmt();
		creditAmt = StringUtils.isBlank(creditAmt) ? "10000" : creditAmt;
		creditAmt = creditAmt.replaceAll(",", "");
		double money = Double.parseDouble(creditAmt);

		DiagnosisResultList result = diagnosisDicService.getResult(creditBill.getIssueBank(), money,
				creditBill.getLast4digit(), creditBill.getUserId(),creditBill.getHolderName());

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate time1 = LocalDate.now().minusMonths(1);
		LocalDate time2 = LocalDate.now().minusMonths(2);
		LocalDate time3 = LocalDate.now().minusMonths(3);
		String format = String.format(TEXT, creditBill.getIssueBank(), creditBill.getHolderName(), creditAmt, "15000",
				df.format(time1), df.format(time1), df.format(time2), df.format(time2), df.format(time3),
				df.format(time3), creditBill.getIssueBank(), creditBill.getIssueBank(), creditBill.getIssueBank());
		String[] strings = format.split("\\r\\n");
		result.setResult(strings);

		return ResultVoUtil.success(result);
	}

	@ApiOperation("查询诊断结果")
	@GetMapping("/get")
	public ResultVo<DiagnosisResultList> getDiagnosisResult(
			@ApiParam(value = "用户id", required = true) @RequestParam(required = true) String userId,
			@ApiParam(value = "银行卡名字", required = true) @RequestParam(required = true) String bankname,
			@ApiParam(value = "卡号后四位", required = true) @RequestParam(required = true) String last4digit) {
		DiagnosisResultList result = diagnosisResultService.getResult(userId, bankname, last4digit);
		return ResultVoUtil.success(result);
	}
	
	@ApiOperation("雷达文案")
	@GetMapping("/radar")
	public ResultVo<String[]> get() {
		String[] strings = RADAR.split("\\r\\n");
		return ResultVoUtil.success(strings);
	}

	@ApiOperation("更新消费金额")
	@PutMapping
	public ResultVo<DiagnosisResultList> update(@RequestParam Integer id, @RequestParam Double money) {
		DiagnosisResultList result = diagnosisResultService.update(id, money);
		return ResultVoUtil.success(result);
	}

	private final static String TEXT = "%s银行, 户主：%s，固定额度：%s，临时额度：%s，系统确认中......\r\n" + "银行基本信息确认完成。\r\n"
			+ "最近3个月消费账单分析中......\r\n" + "%s账单分析中......\r\n" + "%s月账单分析完成。\r\n" + "%s月账单分析中......\r\n"
			+ "%s月账单分析完成。\r\n" + "%s月账单分析中......\r\n" + "%s月账单分析完成。\r\n" + "刷卡消费类型分析中......\r\n"
			+ "刷卡消费类型分析完成。\r\n" + "%s银行诊断模型启动中...\r\n" + "%s银行诊断模型分析中.........\r\n" + "%s银行诊断模型分析完成。\r\n"
			+ "一键诊断完成。";
	
	private final static String RADAR = "数据准备中...\r\n" + 
			"银行卡基本信息确认完成。\r\n" + 
			"最近3个月消费账单分析完成。\r\n" + 
			"刷卡消费类型分析完成。\r\n" + 
			"诊断模型分析完成。\r\n" + 
			"一键诊断完成。";
	
	
}
