package com.lichi.increaselimit.netloan.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 刷卡任务
 * @author majie
 *
 */
@Getter
@Setter
public class CardTaskCount {
	
	private Integer total;
	
	private Integer done;
	
	private Integer undone;

	
}
