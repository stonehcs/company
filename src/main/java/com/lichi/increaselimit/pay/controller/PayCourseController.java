package com.lichi.increaselimit.pay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.course.entity.Course;
import com.lichi.increaselimit.course.service.CourseService;
import com.lichi.increaselimit.pay.config.PayConfig;
import com.lichi.increaselimit.pay.util.InputTostring;
import com.lichi.increaselimit.pay.util.Signature;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.entity.UserCourse;
import com.lichi.increaselimit.user.service.UserCourseService;
import com.lichi.increaselimit.user.service.UserService;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author Rodger.Young 信付宝支付
 */
@RequestMapping("payCourse")
@RestController
@Api(description = "信付宝QQH5支付接口")
public class PayCourseController {
	private static final String PAY_GATEWAY = "/payment/PayApply.do";

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;

	@Autowired
	private UserCourseService userCourseService;
	
	@Autowired
	private HttpServletRequest requset;
	
	@PostMapping("/{courseId}")
	@ApiOperation("用户课程支付")
	public ResultVo<String> pay(@ApiParam(value = "用户token", required = true) @RequestHeader String token,
			@ApiParam(value = "课程的ID", required = true) @PathVariable Integer courseId)
			throws UnsupportedEncodingException {
		String string = redisUtils.get(Constants.LOGIN_USER + token);

		if (StringUtils.isBlank(string)) {
			throw new BusinessException(ResultEnum.LOGIN_TIME_OUT);
		}
		// 获取用户信息
		User user = userService.loadUserInfoByUserId(token);
		Course course = courseService.getCourse(courseId);
		if (course == null) {
			return ResultVoUtil.error(400, "课程不存在");
		}
		if (user == null) {
			return ResultVoUtil.error(400, "用户不存在或token错误");
		}
		UserCourse userCourse=userCourseService.selectUserCourse(token, courseId);
		if (userCourse==null) {
			return ResultVoUtil.error(400, "您还未报名");
		}else if (userCourse!=null&&userCourse.getStatus()==1) {
			return ResultVoUtil.error(500, "您已经交费");
		} 
		// 把request请求的参数放到Map中
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("versionId", "1.0");
		// 金额分为单位
		String payMoney= String.valueOf((int) (course.getMoney() * 100));
		map.put("orderAmount",payMoney);
		map.put("accountType", "0");
		map.put("tranChannel", "103");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(new Date());
		// 获取时间订单
		map.put("orderDate", str);
		map.put("currency", "RMB");
		map.put("accNoType", "A");
		map.put("transType", "008");
		// http://mpecs.zillions.com.cn:10005/smp/users?page=1&row=100
		String aurl=requset.getScheme()+"://"+requset.getServerName()+":"+requset.getServerPort()+"/"+requset.getContextPath();
		System.out.println(aurl);
		map.put("asynNotifyUrl",
				aurl+"/payCourse/success/"+courseId+"?token="+token+"&money="+payMoney);
		map.put("synNotifyUrl", "https%3A%2F%2F123.sogou.com%2F");
		map.put("signType", "MD5");
		// 商户编号
		map.put("merId", PayConfig.merId);
		map.put("prdOrdNo", "100520290" + System.currentTimeMillis());
		// 银行卡号
		map.put("bankCardNo", "00000000519006");
		map.put("payMode", "00033");
		map.put("receivableType", "D00");
		map.put("prdAmt", "1");
		map.put("prdDisUrl", "http%3A%2F%2Fwww.icardpay.com");
		map.put("prdName", user.getNickname() + course.getTitle() + "支付");
		map.put("prdShortName", user.getNickname() + course.getTitle() + "支付");
		map.put("prdDesc", aurl+"payCourse/success?token="+token+"&courseId="+courseId+"&money="+payMoney+"P"+user.getNickname() + course.getTitle() + "支付");
		map.put("pnum", "1");
		map.put("merParam", "");
		map.put("next", "下一步");
		if ("MD5".equalsIgnoreCase(map.get("signType").toString())) {
			// #.md5编码并转成大写 签名：
			String sign = Signature.createSign(map, PayConfig.key);
			map.put("signData", sign);
		}
		OkHttpClient okHttpClient = new OkHttpClient();
		FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			formBodyBuilder.add(entry.getKey(), entry.getValue());
		}
		RequestBody body = formBodyBuilder.build();
		Request request = new Request.Builder().url(PayConfig.trans_url + PAY_GATEWAY).post(body).build();
		Call call = okHttpClient.newCall(request);
		try {
			Response responseBody = call.execute();
			// 请求成功
			if (responseBody.isSuccessful()) {
				return ResultVoUtil.success(InputTostring.delHTMLTag(responseBody.body().string()));
			} else {
				return ResultVoUtil.error(500, "网络超时");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultVoUtil.error(500, "错误");

	}
	@PostMapping("/success/{courseId}")
	@ApiIgnore
	public String paySuccess(
			@ApiParam(name="用户token",required=true)@RequestParam String token,
			@ApiParam(name="课程ID",required=true)@PathVariable Integer courseId,
			@ApiParam(name="支付金额",required=true)@RequestParam Double money) {
		courseService.coursePay(courseId, token, money/100);
		return "SUCCESS";
	}

}
