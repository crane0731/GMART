package gmart.gmart.domain;

import gmart.gmart.command.CreateItemCommand;
import gmart.gmart.command.UpdateItemCommand;
import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.*;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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
    @Column(name = "item_id")
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

    @org.hibernate.annotations.Comment("건담 등급")
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private GundamGrade grade;

    @org.hibernate.annotations.Comment("상품 가격")
    @Column(name = "item_price")
    private Long itemPrice;

    @org.hibernate.annotations.Comment("배송비")
    @Column(name = "delivery_price")
    private Long deliveryPrice;


    @org.hibernate.annotations.Comment("조회 수")
    @Column(name = "view_count")
    private Long viewCount;

    @org.hibernate.annotations.Comment("찜한 수")
    @Column(name = "favorite_count")
    private Long favoriteCount;

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


    @Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemGundam> itemGundams = new ArrayList<>();

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Report> reportItems = new ArrayList<>();

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
        item.grade= command.getGundamGrade();

        item.assemblyStatus=command.getAssemblyStatus();
        item.boxStatus=command.getBoxStatus();
        item.paintStatus=command.getPaintStatus();

        item.saleStatus=SaleStatus.SALE;
        item.reportedStatus=ItemReportedStatus.NOT_REPORTED;

        item.viewCount=0L;
        item.favoriteCount=0L;
        item.reportedCount=0L;
        item.deleteStatus=DeleteStatus.UNDELETED;

        return item;
    }


    /**
     * [업데이트 메서드]
     * @param command 업데이트 커맨드
     */
    public void update(UpdateItemCommand command){

        this.title = command.getTitle();
        this.content = command.getContent();
        this.itemPrice = command.getItemPrice();
        this.deliveryPrice = command.getDeliveryPrice();
        this.assemblyStatus = command.getAssemblyStatus();
        this.boxStatus = command.getBoxStatus();
        this.paintStatus = command.getPaintStatus();
        this.grade=command.getGundamGrade();

    }

    /**
     * [연관관계 편의 메서드]
     * @param store 상점 엔티티
     */
    private void setStore(Store store) {
        this.store = store;
        store.getItems().add(this);
    }

    /**
     * [비즈니스 로직]
     * 판매 상태 변경
     */
    public void changeSaleStatus(SaleStatus saleStatus){
        this.saleStatus=saleStatus;
    }

    /**
     * [비즈니스 로직]
     * 모든 상품 이미지 제거
     */
    public void removeAllItemImages(){
        List<ItemImage> copy = new ArrayList<>(itemImages);
        for (ItemImage itemImage : copy) {
            itemImage.removeItem();
        }
    }

    /**
     * [비즈니스 로직]
     * 모든 상품 건담 제거
     */
    public void removeAllItemGundams(){
        List<ItemGundam> copy = new ArrayList<>(itemGundams);
        for (ItemGundam itemGundam : copy) {
            itemGundam.removeItem();
        }
    }

    /**
     * [비즈니스 로직]
     * 조회수 증가
     */
    public void plusViewCount(){
        this.viewCount++;
    }

    /**
     * [비즈니스 로직]
     * 상품 삭제(비활성화) 처리
     */
    public void softDelete(){
        //이미 삭제 상태인지 확인
        validateDeleted();
        //삭제 처리
        this.deleteStatus=DeleteStatus.DELETED;
    }

    /**
     * [비즈니스 로직]
     * 관심등록된 수 증가
     */
    public void plusFavoriteCount(){
        this.favoriteCount++;
    }

    /**
     * [비즈니스 로직]
     * 관심 등록된 수 감소
     */
    public void minusFavoriteCount(){
        if(favoriteCount>0){
            this.favoriteCount--;
        }
    }

    /**
     * [비즈니스 로직]
     * 신고받은 수 증가
     */
    public void plusReportCount(){
        this.reportedCount++;
    }

    /**
     * [비즈니스 로직]
     * 신고받은 수 감소
     */
    public void minusReportCount(){
        if(reportedCount>0){
            this.reportedCount--;
        }
    }

    /**
     * [비즈니스 로직]
     * 상품 신고 받음 처리
     */
    public void reported(){
        this.reportedStatus=ItemReportedStatus.REPORTED;
        plusReportCount();
    }



    //==이미 삭제 상태인지 확인 하는 로직==//
    private void validateDeleted() {
        if(this.deleteStatus.equals(DeleteStatus.DELETED)){
            throw new ItemCustomException(ErrorMessage.ALREADY_DELETE);
        }
    }



}
