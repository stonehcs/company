package com.lichi.increaselimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * 项目入口
 * 
 * @author majie
 *
 */
@SpringBootApplication
@ServletComponentScan
@ImportResource(locations = { "classpath:druid-bean.xml" })
@EnableAsync
public class IncreaseLimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncreaseLimitApplication.class, args);
	}
	
    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(60000);
        httpRequestFactory.setConnectTimeout(60000);
        httpRequestFactory.setReadTimeout(60000);

        return new RestTemplate(httpRequestFactory);
    }

}
