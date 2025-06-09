package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 - 건담 중간 테이블
 */
@Entity
@Table(name = "item_gundam")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemGundam extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("상품 건담 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_gundam_id")
    private Long id;

    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @org.hibernate.annotations.Comment("건담 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gundam_id")
    private Gundam gundam;

    @org.hibernate.annotations.Comment("수량")
    @Column(name = "count")
    private Long count;

    /**
     * [생성 메서드]
     * @param item 상품 엔티티
     * @param gundam 건담 엔티티
     * @return ItemGundam 상품 건담 엔티티
     */
    public static void create(Item item, Gundam gundam) {
        ItemGundam itemGundam = new ItemGundam();
        itemGundam.setItem(item);
        itemGundam.gundam = gundam;
    }

    /**
     * [연관 관계 편의 메서드]
     * @param item 상품 엔티티
     */
    private void setItem(Item item) {
        this.item = item;
        item.getItemGundams().add(this);
    }

}
