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

    @OneToOne(mappedBy = "store")
    private StoreProfileImage profileImage=new StoreProfileImage();


}
