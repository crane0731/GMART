package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상점 프로필 이미지
 */
@Entity
@Table(name = "store_profile_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreProfileImage extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("상점 프로필 이미지 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_profile_image_id")
    private Long id;

    @org.hibernate.annotations.Comment("이미지 URL")
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * [생성 메서드]
     * @param imageUrl 이미지 URL
     * @return StoreProfileImage 상점 프로필 이미지 엔티티
     */
    public static StoreProfileImage create(String imageUrl) {
        StoreProfileImage storeProfileImage = new StoreProfileImage();
        storeProfileImage.imageUrl = imageUrl;
        return storeProfileImage;
    }

}
