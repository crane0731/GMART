package gmart.gmart.config;

import org.springframework.context.annotation.Configuration;
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

        registry.addResourceHandler("/article-images/**")
                .addResourceLocations("file:///C:/Users/dlwns/crane/PROJECT_GMART/Spring Boot/ArticleImage/");

        registry.addResourceHandler("/store-images/**")
                .addResourceLocations("file:///C:/Users/dlwns/crane/PROJECT_GMART/Spring Boot/StoreImage/");
    }
}
