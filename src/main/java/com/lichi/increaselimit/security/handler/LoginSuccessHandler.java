package com.lichi.increaselimit.security.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功处理 创建oauth2.0token
 * 
 * @author majie
 *
 */
@Component("loginSuccessHandler")
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		User user = (User) authentication.getPrincipal();
		log.info("登录成功,用户id:{}", user.getId());

		if(StringUtils.isBlank(user.getMobile())) {
			userService.deleteByPrimary(user.getId());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.error(401, "内测阶段暂不支持第三方登录")));
			return;
		}
		
		if (!list.contains(user.getMobile())) {
			userService.deleteByMobile(user.getMobile());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.error(401, "目前只开放内测用户使用")));
			return;
		}

		// 判断是不是第一次登陆,如果是要去注册环信,并更新pid的转换人数
		Date updateTime = user.getUpdateTime();
		if (updateTime == null) {
			userService.registerHuanxinAndUpdatePid(user.getId(), user.getPid());
		}

		String token = user.getId();

		// 将生成的token放入redis,token设置为永久
		redisUtils.set(Constants.LOGIN_USER + token, token);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("token", token);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ResultVoUtil.success(jsonObject)));
	}

	public static List<String> list = Arrays.asList("15972285835", "18171291486", "13995969222", "13872059777",
			"18684786001", "15872410405", "15872224599", "15997201176", "18571785200", "18627870189", "18971112626",
			"18972801810", "18265144567", "17702765417", "18685102678", "13667677321", "13883347721", "13872522543",
			"13883790125", "18203056114", "13778806444", "15037703535", "15223856665", "17737007114", "13308644134",
			"18223464560", "13709460543", "15172080152", "18696740225", "18963919666", "17710461419", "17771873660",
			"13883131800", "13347391558", "18627167800", "17382273148", "15178977666", "13034430000", "15971360053",
			"13311273858", "18696431777", "13971070275", "13698370437", "18734898859", "17784316244", "15982609919",
			"13872160996", "18185299599", "18723039133", "13812036639", "13908648609", "15523418333", "18228557676",
			"18972409968", "18573138731", "18986111209", "13264881972", "13996315352", "13500383633", "18775927926",
			"13193195584", "13908383098", "18983439189", "15696212822", "13659832692", "18971557107", "13801138799",
			"13100227888", "13355667667", "13657611313", "17786231419", "13983384265", "13509114539", "13716406897",
			"13883201831", "18086613128", "13896194855", "18908611303", "15874880513", "13807232088", "13312207999",
			"13807196196", "15012688613", "13677622639", "13636246858", "15926313363", "13349393391", "13650592818",
			"13597633142", "13883332224", "18607178882", "15512902367", "18043197979", "18523449288", "18696682580",
			"13971319822", "13658398999", "18580238833", "13587208126", "13975851495",
			
			
			"13330268286","18323299034","18875021822","15922989373","18983280359","17749974977","13108991934","18716445927","13896238685","18723599460",
			"17602381131","18591605914","17782327704");
	
}