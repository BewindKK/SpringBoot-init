package com.bewind.springbootstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("dev")
public class Knife4jConfiguration {

    @Value("${swagger.enable}")
    private Boolean enable;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("project-backend")
                                .description("project-backend")
                                .version("1.0")
                                .build()
                )
                //分组名称
                .groupName("3.X版本")
                .select()
                //这里指定你自己的Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.bewind.springbootstart.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
