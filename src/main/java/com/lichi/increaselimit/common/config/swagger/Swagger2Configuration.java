package com.lichi.increaselimit.common.config.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 * @author majie
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	@Bean
	public Docket accessToken() {
		
        ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("String")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
        
		// 定义组
		return new Docket(DocumentationType.SWAGGER_2).groupName("api")
				// 选择那些路径和 api 会生成 document
				.select() 

				// 拦截的包路径
				.apis(RequestHandlerSelectors.basePackage("com.lichi.increaselimit")) 
				// 拦截的接口路径
				.paths(PathSelectors.any())
				.build() 
				// 配置说明
				.apiInfo(apiInfo()); 
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("骊驰项目组")
				.description("api开发文档")
				.termsOfServiceUrl("https://git.oschina.net/dashboard/projects")
//				.contact(new Contact("lichi", "", ""))
//				 .license("Apache License Version 2.0")
//				 .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
				.version("1.0")
				.build();
	}
}