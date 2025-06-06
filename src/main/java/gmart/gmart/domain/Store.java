package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상점
 */
@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("상점 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("상점 프로필 이미지 아이디")
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "store_profile_image_id")
    private StoreProfileImage storeProfileImage;

    @org.hibernate.annotations.Comment("상점 이름")
    @Column(name = "name")
    private String name;

    @org.hibernate.annotations.Comment("상점 설명")
    @Column(name = "introduction")
    private String introduction;

    @org.hibernate.annotations.Comment("상점 운영 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StoreStatus status;

    @org.hibernate.annotations.Comment("상품 수")
    @Column(name = "item_count")
    private Long itemCount;

    @org.hibernate.annotations.Comment("신고 받은 수")
    @Column(name = "reported_count")
    private Long reportedCount;

    @org.hibernate.annotations.Comment("총 방문 수")
    @Column(name = "total_visit_count")
    private Long totalVisitCount;

    @org.hibernate.annotations.Comment("리뷰 수")
    @Column(name = "reviewed_count")
    private Long reviewedCount;

    @org.hibernate.annotations.Comment("점수")
    @Column(name = "rating")
    private Long rating;

    @org.hibernate.annotations.Comment("찜한 수")
    @Column(name = "favorite_count")
    private Long favoriteCount;


    /**
     * [생성 메서드]
     * @param name 상점 이름
     * @param introduction 상점 소개
     * @param storeProfileImage 상점 프로필 이미지
     * @return Store 상점 엔티티
     */
    public static Store create(Member member, String name, String introduction,StoreProfileImage storeProfileImage){
        Store store = new Store();
        store.name = name;
        store.introduction = introduction;
        store.storeProfileImage = storeProfileImage;
        store.member=member;
        store.status = StoreStatus.ACTIVE;
        store.itemCount = 0L;
        store.reportedCount = 0L;
        store.totalVisitCount = 0L;
        store.reviewedCount = 0L;
        store.rating = 0L;
        store.favoriteCount = 0L;
        return store;

    }

    /**
     * [업데이트 메서드]
     * @param name 상점 이름
     * @param introduction 상점 소개글
     * @param storeProfileImage 상점 프로필 이미지 엔티티
     */
    public void update(String name, String introduction,StoreProfileImage storeProfileImage){
        this.name = name;
        this.introduction = introduction;
        this.storeProfileImage = storeProfileImage;
    }

}
