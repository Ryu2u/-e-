package com.ryuzu.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2 配置类
 * @author Ryuzu
 * @date 2022/2/19 16:56
 */


@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ryuzu.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("云e办")
                .description("云e办接口文档")
                .contact(new Contact("ryuzu", "http://localhost:8081/doc.html", "ryu2u@qq.com"))
                .version("1.0")
                .build();
    }

    public List<ApiKey> securitySchemes(){
        // 设置请求头
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "Header");
        result.add(apiKey);
        return result;
    }

    public List<SecurityContext> securityContexts(){
        // 设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;

    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultPath())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();

    }

    private List<SecurityReference> defaultPath() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorization = new AuthorizationScope[1];
        authorization[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorization));
        return result;
    }


}
