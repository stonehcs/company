package com.lichi.increaselimit.user.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.user.entity.LoginUser;
import com.lichi.increaselimit.user.service.LoginUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 测试controller
 * 
 * @author majie
 *
 */
@Api(description = "登陆的客服")
@RestController
@RequestMapping("/login-user")
public class LoginUserController {
	
	@Autowired
	private LoginUserService loginUserService;
	
	/**
	 * 获取所有的后台登陆用户
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("随机获取一个客服")
	public ResultVo<LoginUser> getAllLoginUser(@PathVariable String id) {
		
		List<LoginUser> list = loginUserService.getAll();
		
		if(null == list || list.size() == 0) {
			throw new BusinessException(ResultEnum.NO_ONLINE_USER);
		}
		Collections.shuffle(list);
		return ResultVoUtil.success(list.get(0));
	}

}
