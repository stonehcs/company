package com.lichi.increaselimit.user.service;

import com.lichi.increaselimit.user.entity.VipLevel;

public interface VipLevelService {

	/**
	 * 获取vip等级对应的奖金比例
	 * @param vipLevel
	 * @return
	 */
	VipLevel getLevelPercent(Integer vipLevel);

}
