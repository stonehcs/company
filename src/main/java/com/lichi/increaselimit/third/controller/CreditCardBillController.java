package com.lichi.increaselimit.third.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.IdUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.third.entity.CreditBill;
import com.lichi.increaselimit.third.entity.CreditBillDetail;
import com.lichi.increaselimit.third.entity.CreditBillVo;
import com.lichi.increaselimit.third.entity.UserEmail;
import com.lichi.increaselimit.third.service.CreditBillService;
import com.lichi.increaselimit.third.service.UserEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 信用卡账单查询
 * 
 * @author majie
 *
 */
@RestController
@RequestMapping("/bill")
@Api(description = "信用卡账单查询")
public class CreditCardBillController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CreditBillService creditBillService;
	@Autowired
	private UserEmailService userEmailService;

	private static final String URL = "https://way.jd.com/creditsaas/get_creditcard_statements";

	private static final String APPKEY = "c78285411a06e4a7196df56144a89bb8";

	@ApiOperation("第一次输入email时候调用")
	@PostMapping
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object setEmail(@ApiParam(value = "用户id", required = true) @RequestParam(required = true) String userId,
			@ApiParam(value = "邮箱", required = true) @RequestParam(required = true) String username,
			@ApiParam(value = "密码", required = true) @RequestParam(required = true) String password) {

		try {
			UserEmail userEmail = userEmailService.selectByPrimaryKey(username);
			
			if(userEmail != null) {
				throw new BusinessException(ResultEnum.EMAIL_EXSIT);
			}
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("email", username);
			map.add("password", password);
			map.add("appkey", APPKEY);

			UserEmail email = new UserEmail();
			email.setEmail(username);
			email.setPassword(password);
			email.setUserId(userId);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			JSONObject jsonObject = restTemplate.postForObject(URL, request, JSONObject.class);

			if ("10000".equals(jsonObject.getString("code"))) {
				HashMap<String, Object> hashmap = (LinkedHashMap) jsonObject.get("result");
				String code = hashmap.get("code").toString();
				if ("1002".equals(code)) {
					return jsonObject;
				}
				List list = (ArrayList) hashmap.get("result");
				List<CreditBillVo> listvo = new ArrayList<>();
				list.stream().forEach(e -> {
					CreditBillVo vo = new CreditBillVo();
					MapToBean(e, vo, userId);
					listvo.add(vo);
				});
				creditBillService.insert(listvo, email);
			} else {
				return jsonObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ResultEnum.NO_RESPONSE);
		}

		return ResultVoUtil.success();
	}

	@ApiOperation("获取邮箱列表")
	@GetMapping("/email/{userId}")
	public Object getEmailList(@PathVariable String userId) {
		List<UserEmail> list = userEmailService.getList(userId);
		return ResultVoUtil.success(list);
	}

	@ApiOperation("通过用户id获取未还款信息")
	@GetMapping("/{userId}")
	public Object getCreditCardBill(@PathVariable String userId,
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		if(StringUtils.isBlank(userId)) {
			return ResultVoUtil.success();
		}
		PageInfo<CreditBill> info = creditBillService.selectByUserId(userId, page, size);
		return ResultVoUtil.success(info);
	}

	@ApiOperation("通过银行名字和后四位查询信用卡信息")
	@GetMapping
	public Object get(@ApiParam(value = "用户id", required = true) @RequestParam(required = true) String userId,
			@ApiParam(value = "银行名字", required = true) @RequestParam(required = true) String issueBank,
			@ApiParam(value = "持卡人名字", required = true) @RequestParam(required = true) String holderName,
			@ApiParam(value = "银行卡后四位", required = true) @RequestParam(required = true) String last4digit,
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		PageInfo<CreditBill> info = creditBillService.selectBank(userId, issueBank, holderName, last4digit, page, size);
		return ResultVoUtil.success(info);
	}

	@ApiOperation("通过账单主键获取账单详情")
	@GetMapping("/detail/{billId}")
	public Object get(@PathVariable String billId,
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		PageInfo<CreditBillDetail> info = creditBillService.selectBillDetail(billId,page,size);
		return ResultVoUtil.success(info);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void MapToBean(Object e, CreditBillVo vo, String userId) {
		Map<String, Object> objMap = (LinkedHashMap) e;
		List map_detail = (ArrayList) objMap.get("statement_detail");

		String balanceRmb = (String) (objMap.getOrDefault("balance_rmb", ""));
		String issueBank = (String) objMap.getOrDefault("issue_bank", "");
		String holderName = (String) objMap.getOrDefault("holder_name", "xxx");
		String last4digit = (String) objMap.getOrDefault("last4digit", "");
		String paymentDueDate = (String) objMap.getOrDefault("payment_due_date", "");
		// 替换/
		paymentDueDate = replace(paymentDueDate);
		String statementEndDate = (String) objMap.getOrDefault("statement_end_date", "");
		statementEndDate = replace(statementEndDate);
		String statementDate = (String) objMap.getOrDefault("statement_date", "");
		statementDate = replace(statementDate);
		String statementStartDate = (String) objMap.getOrDefault("statement_start_date", "");
		statementStartDate = replace(statementStartDate);
		String minPaymentRmb = (String) objMap.getOrDefault("min_payment_rmb", "");
		String availablePoints = (String) objMap.getOrDefault("available_points", "");
		String creditAmt = (String) objMap.getOrDefault("credit_amt", "");
		String cashLimitAmt = (String) objMap.getOrDefault("cash_limit_amt", "");
		CreditBill creditBill = new CreditBill();
		String id = IdUtils.getId();
		creditBill.setId(id);
		creditBill.setBalanceRmb(balanceRmb);
		creditBill.setIssueBank(issueBank);
		creditBill.setHolderName(holderName);
		creditBill.setLast4digit(last4digit);
		creditBill.setPaymentDueDate(paymentDueDate);

		if (StringUtils.isBlank(statementEndDate) && !StringUtils.isBlank(paymentDueDate)) {
			LocalDate date1 = LocalDate.parse(paymentDueDate);

			LocalDate date = date1.minusMonths(1);
			statementEndDate = date.toString();
		}
		creditBill.setStatementEndDate(statementEndDate);
		creditBill.setStatementStartDate(statementStartDate);
		creditBill.setMinPaymentRmb(minPaymentRmb);
		creditBill.setAvailablePoints(availablePoints);
		creditBill.setCreditAmt(creditAmt);
		creditBill.setCashLimitAmt(cashLimitAmt);
		creditBill.setUserId(userId);
		creditBill.setStatementDate(statementDate);

		// 初始化还款日
		if ("交通银行".equals(issueBank) && !StringUtils.isBlank(statementEndDate)) {
			LocalDate date1 = LocalDate.parse(statementEndDate);
			LocalDate date = date1.minusMonths(1);
			statementEndDate = date.toString();
			creditBill.setStatementDate(statementEndDate);
		}

		// 初始化外层的银行卡后四位
		if ("中国民生银行".equals(issueBank)) {
			Long credit_card_stmt_id = (Long) objMap.getOrDefault("credit_card_stmt_id", "");
			map_detail.stream().forEach(a -> {

				LinkedHashMap map = ((LinkedHashMap) a);
				Long innerlong = (Long) map.getOrDefault("credit_card_stmt_id", "");

				if (credit_card_stmt_id.equals(innerlong)) {
					String innerlast4digit = (String) map.getOrDefault("last4digit", "");

					creditBill.setLast4digit(innerlast4digit);

				}

			});
		}

		// 初始化开始时间
		if (StringUtils.isBlank(statementStartDate) && !StringUtils.isBlank(statementEndDate)) {
			LocalDate date1 = LocalDate.parse(statementEndDate);
			LocalDate date = date1.minusMonths(1);
			statementStartDate = date.toString();
			creditBill.setStatementStartDate(statementStartDate);
		}

		// 初始化免息期
		if (StringUtils.isBlank(paymentDueDate) || StringUtils.isBlank(statementStartDate)) {
			creditBill.setFreeDay(-1);
		} else {
			LocalDate date1 = LocalDate.parse(paymentDueDate);
			LocalDate date2 = LocalDate.parse(statementStartDate);

			long until = date2.until(date1, ChronoUnit.DAYS);
			creditBill.setFreeDay((int) until);
		}

		List<CreditBillDetail> detail = getDetail(map_detail, id);

		vo.setCreditBill(creditBill);
		vo.setCreditBillDetail(detail);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<CreditBillDetail> getDetail(List details, String id) {
		List<CreditBillDetail> creditBillDetail = new ArrayList<>();

		details.stream().forEach(e -> {
			CreditBillDetail detail = new CreditBillDetail();
			Map<String, Object> map_detail = (LinkedHashMap) e;
			String transDate = (String) map_detail.getOrDefault("trans_date", "");
			transDate = replace(transDate);
			String statementStartDate = (String) map_detail.getOrDefault("statement_start_date", "");
			statementStartDate = replace(statementStartDate);
			String statementEndDate = (String) map_detail.getOrDefault("statement_end_date", "");
			statementEndDate = replace(statementEndDate);
			String last4digit = (String) map_detail.getOrDefault("last4digit", "");
			String postCurrency = (String) map_detail.getOrDefault("post_currency", "");
			if ("CNY".equals(postCurrency)) {
				postCurrency = "RMB";
			}
			String transDesc = (String) map_detail.getOrDefault("trans_desc", "");
			String postAmt = (String) map_detail.getOrDefault("post_amt", "");
			detail.setCreditBillId(id);
			detail.setLast4digit(last4digit);
			detail.setPostAmt(postAmt);
			detail.setPostCurrency(postCurrency);
			detail.setStatementEndDate(statementEndDate);
			detail.setStatementStartDate(statementStartDate);
			detail.setTransDesc(transDesc);
			detail.setTransDate(transDate);
			creditBillDetail.add(detail);
		});
		return creditBillDetail;
	}

	private String replace(String date) {
		if (date.contains("/")) {
			date = date.replace("/", "-");
		}
		return date;
	}

}
