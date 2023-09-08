//package com.ssafy.confidentIs.keytris.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
////	http://localhost:8080/swagger-ui/index.html
//
//    private String version = "V1";
//    private String title = "Keytris API " + version;
////
//    private ApiInfo swaggerInfo() {
//        return new ApiInfoBuilder()
//                .title(title)
//                .version(version)
//                .description("Keytris Api 문서입니다")
//                .build();
//    }
//
//    @Bean
//    public Docket swaggerApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(swaggerInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("com.ssafy.confidentIs.keytris.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//}
//
//
//
