package gmart.gmart.domain;

import gmart.gmart.command.CreateItemCommand;
import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.item.CreateItemRequestDto;
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
    @Column(name = "reported_status")
    private ItemReportedStatus reportedStatus;

    //거래 방법
    @org.hibernate.annotations.Comment("거래 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type")
    private DealType dealType;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemGundam> itemGundams = new ArrayList<>();

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    /**
     * [생성 메서드]
     * @param store 상점 엔티티
     * @param command 상품 엔티티 생성 커맨드
     * @return Item 상품 엔티티
     */
    public static Item create(Store store, CreateItemCommand command){
        Item item = new Item();

        item.setStore(store);
        item.title = command.getTitle();
        item.content = command.getContent();
        item.itemPrice = command.getItemPrice();
        item.deliveryPrice = command.getDeliveryPrice();
        item.location = command.getLocation();

        item.assemblyStatus=command.getAssemblyStatus();
        item.boxStatus=command.getBoxStatus();
        item.paintStatus=command.getPaintStatus();
        item.dealType=command.getDealType();

        item.saleStatus=SaleStatus.SALE;
        item.reportedStatus=ItemReportedStatus.NOT_REPORTED;

        item.viewCount=0L;
        item.favoriteCount=0L;
        item.chattingCount=0L;
        item.reportedCount=0L;

        return item;
    }



    /**
     * [연관관계 편의 메서드]
     * @param store 상점 엔티티
     */
    private void setStore(Store store) {
        this.store = store;
        store.getItems().add(this);
    }

}
