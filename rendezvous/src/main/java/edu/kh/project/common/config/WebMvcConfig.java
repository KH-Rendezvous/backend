package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:config.properties") // 아까 만든 설정 파일 읽기
public class WebMvcConfig implements WebMvcConfigurer {

    // 설정 파일에서 값 가져오기
    @Value("${project.resource.webpath}")
    private String webPath; // "/images/"
    
    @Value("${project.resource.location}")
    private String resourceLocation; // "file:///C:/upload/project/"

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 로 시작하는 요청이 오면 -> 실제 파일 경로로 연결
        registry.addResourceHandler(webPath + "**")
                .addResourceLocations("file:///" + resourceLocation);
    }
}