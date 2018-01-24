package com.lichi.increaselimit.pay.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Rodger.Young
 * 支付配置
 */
public class PayConfig {
	static Properties prop;

	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 商户号
     public static String merId;
	// 商户的私钥
	public static String key;
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 字符编码格式 目前支持 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	
	public static String trans_url = "";
	
	
	static{
		try {
			prop = new Properties();
			InputStream in = PayConfig.class.getClassLoader().getResourceAsStream("payconfig.properties");
			prop.load(in);
			key = prop.getProperty("KEY");
			merId = prop.getProperty("MERID");
			input_charset = prop.getProperty("INPUT_CHARSET");
			sign_type = prop.getProperty("SIGN_TYPE");
			trans_url = prop.getProperty("TRANS_URL");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
