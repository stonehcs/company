package com.lichi.increaselimit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

/**
 * 测试类
 * @author majie
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IncreaseLimitApplicationTest {

	@Test
	public void contextLoads() {
	}
	
	public static void main(String[] args) {
		Gson g = new Gson();
		String json = g.toJson("<p class=\"ql-align-justify\"><span style=\"color: rgb(89, 89, 89);\">\"不小心成了“黑户”，上了征信可咋办？今天小优就来和大家聊聊这个问题。\"</span></p>");
		System.out.println(json);
	}

}
