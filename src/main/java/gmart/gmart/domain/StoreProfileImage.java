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

    @org.hibernate.annotations.Comment("상점 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @org.hibernate.annotations.Comment("이미지 URL")
    @Column(name = "image_url")
    private String imageUrl;

}
