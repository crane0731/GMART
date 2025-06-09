package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.IsMain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 이미지
 */
@Entity
@Table(name = "item_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("상품 이미지 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @org.hibernate.annotations.Comment("이미지 URL")
    @Column(name = "image_url",nullable = false)
    private String imageUrl;

    @org.hibernate.annotations.Comment("대표 이미지 여부")
    @Enumerated(EnumType.STRING)
    @Column(name = "is_main")
    private IsMain isMain;


    /**
     * [생성 메서드]
     * @param imageUrl 이미지 URL
     * @param isMain 대표 이미지 여부
     * @return ItemImage 상품이미지 엔티티
     */
    public static void create(Item item,String imageUrl, IsMain isMain) {
        ItemImage itemImage = new ItemImage();

        //연관관계 세팅
        itemImage.setItem(item);

        itemImage.imageUrl = imageUrl;
        itemImage.isMain = isMain;
    }

    /**
     * [연관 관계 편의 메서드]
     * @param item 상품 엔티티
     */
    private void setItem(Item item) {
        this.item = item;
        item.getItemImages().add(this);
    }
}
