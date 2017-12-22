package com.lichi.increaselimit.user.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.Constants;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.RedisUtils;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试controller
 * 
 * @author majie
 *
 */
@Api(description = "登陆的客服")
@RestController
@RequestMapping("/login-user")
@Slf4j
public class LoginUserController {
	
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 获取所有的后台登陆用户
	 * @return
	 */
	@GetMapping
	@ApiOperation("随机获取一个客服")
	public ResultVo<String> getAllLoginUser() {
		
		log.info("随机获取一个客服");
		
		Set<String> keys = redisUtils.getKeys(Constants.LOGIN_KEFU + "*");
		
		if(null == keys || keys.size() == 0) {
			throw new BusinessException(ResultEnum.NO_ONLINE_USER);
		}
		List<String> list = keys.stream().collect(Collectors.toList());
		Collections.shuffle(list);
		return ResultVoUtil.success(list.get(0));
	}

}
