package com.lichi.increaselimit.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;

/**
 * 环信
 * @author majie
 *
 */
@Component
public class HuanXinUtils {

	private static final String ORGNAME	= "1105171201115139";
	private static final String APPNAME	= "lichi-happy";
	private static final String CLIENT_ID	= "YXA6kWuM0NZyEeeAsMOBJBNmJw";
	private static final String CLIENT_SECERT	= "YXA6K_THB0q1HXvAXhpNrPYJwSntCOk";
	
	private static final String HX_POST_ACCESSTOKEN_URL = "http://a1.easemob.com/"+ORGNAME+"/"+APPNAME+"/token";
	
	private static final String HX_REGISTER_URL = "http://a1.easemob.com/"+ORGNAME+"/"+APPNAME+"/users";
	
	/**
	 * 注册环信用户
	 */
	public static void registerUser(String userId,RestTemplate restTemplate) {
		//拼装获取token数据
		Map<String, String> map = new HashMap<String, String>();
		map.put("grant_type", "client_credentials");
		map.put("client_id", CLIENT_ID);
		map.put("client_secret", CLIENT_SECERT);
		
		JSONObject postForObject = restTemplate.postForObject(HX_POST_ACCESSTOKEN_URL, map, JSONObject.class);
		
		String accessToken = postForObject.get("access_token").toString();
		
		//该方法通过restTemplate请求远程restfulAPI
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject parm = new JSONObject();
        parm.put("username", userId);
        parm.put("password", "123456");
        HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(parm, headers);
        HttpEntity<JSONObject> response = restTemplate.exchange(HX_REGISTER_URL, HttpMethod.POST, entity, JSONObject.class);

        //没有注册成功抛出异常回滚
        if(null != response.getBody().getString("error")) {
			throw new BusinessException(ResultEnum.REGISTER_ERROR);
		}
	}
}
