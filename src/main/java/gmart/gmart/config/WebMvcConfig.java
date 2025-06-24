package gmart.gmart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WEBMVC 설정 클래스
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations("file:///C:/Users/dlwns/crane/PROJECT_GMART/Spring Boot/ProfileImage/");

        registry.addResourceHandler("/store-images/**")
                .addResourceLocations("file:///C:/Users/dlwns/crane/PROJECT_GMART/Spring Boot/StoreImage/");

        registry.addResourceHandler("/item-images/**")
                .addResourceLocations("file:///C:/Users/dlwns/crane/PROJECT_GMART/Spring Boot/ItemImage/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins("http://localhost:5173") // Vue dev 서버 주소 (포트 확인!)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 인증정보(쿠키 등) 허용
    }
}
