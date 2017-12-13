package com.lichi.increaselimit.common.utils;

/**
 * 生成用户id
 * 
 * @author majie
 *
 */
public class IdUtils {

	public static SnowflakeIdWorker SNOWFLAKEIDWORKER = new SnowflakeIdWorker(0, 0);

	public static String getId() {
		return SNOWFLAKEIDWORKER.nextId() + "";
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getId());
		}
	}
}
