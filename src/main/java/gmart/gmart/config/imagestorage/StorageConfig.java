package gmart.gmart.config.imagestorage;

import gmart.gmart.service.image.LocalStorageService;
import gmart.gmart.service.image.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 이미지 저장 관련 설정
 */
@Configuration
public class StorageConfig {

    @Value("${file.upload-profile-dir}")
    private String profileDir;

    @Value("${file.upload-article-dir}")
    private String articleDir;

    @Value("${file.upload-store-dir}")
    private String storeDir;

    @Value("${file.upload-item-dir}")
    private String itemDir;

    @Bean
    public StorageService profileImageStorageService() {
        return new LocalStorageService(profileDir);
    }

    @Bean
    public StorageService articleImageStorageService() {
        return new LocalStorageService(articleDir);
    }

    @Bean
    public StorageService storeImageStorageService() {
        return new LocalStorageService(storeDir);
    }

    @Bean
    public StorageService itemImageStorageService() {
        return new LocalStorageService(itemDir);
    }


}
