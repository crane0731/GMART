package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 도메인
 */
@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("상품 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @org.hibernate.annotations.Comment("상점 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @org.hibernate.annotations.Comment("상품 제목")
    @Column(name = "title")
    private String title;

    @org.hibernate.annotations.Comment("내용")
    @Column(name = "content")
    private String content;

    @org.hibernate.annotations.Comment("상품 가격")
    @Column(name = "item_price")
    private Long itemPrice;

    @org.hibernate.annotations.Comment("배송비")
    @Column(name = "delivery_price")
    private Long deliveryPrice;

    @org.hibernate.annotations.Comment("거래 장소")
    @Column(name = "location")
    private String location;

    @org.hibernate.annotations.Comment("조회 수")
    @Column(name = "view_count")
    private Long viewCount;

    @org.hibernate.annotations.Comment("찜한 수")
    @Column(name = "favorite_count")
    private Long favoriteCount;

    @org.hibernate.annotations.Comment("채팅 수")
    @Column(name = "chatting_count")
    private Long chattingCount;

    @org.hibernate.annotations.Comment("신고 받은 수")
    @Column(name = "reported_count")
    private Long reportedCount;

    //판매 상태
    @org.hibernate.annotations.Comment("판매 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status")
    private SaleStatus saleStatus;

    //조립 상태
    @org.hibernate.annotations.Comment("조립 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "assembly_status")
    private AssemblyStatus assemblyStatus;

    //박스 상태
    @org.hibernate.annotations.Comment("박스 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "box_status")
    private BoxStatus boxStatus;

    // 도색 상태
    @org.hibernate.annotations.Comment("도색 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "paint_status")
    private PaintStatus paintStatus;

    //신고 상태
    @org.hibernate.annotations.Comment("신고 상태 ")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    //거래 방법
    @org.hibernate.annotations.Comment("신고 상태 ")
    @Enumerated(EnumType.STRING)
    private DealType dealType;


    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemGundam> itemGundams = new ArrayList<>();



}
