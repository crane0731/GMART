package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관심 상품
 */
@Entity
@Table(
        name = "favorite_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_favorite_item_member_item",
                        columnNames = {"member_id", "item_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteItem extends BaseTimeEntity {


    @org.hibernate.annotations.Comment("관심 상품 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_item_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param item 상품 엔티티
     * @return FavoriteItem 관심 상품 엔티티
     */
    public static FavoriteItem create(Member member, Item item) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setMember(member);
        favoriteItem.item=item;
        favoriteItem.deleteStatus=DeleteStatus.UNDELETED;
        return favoriteItem;
    }

    /**
     * [연관 관계 편의 메서드]
     * @param member 회원 엔티티
     */
    private void setMember(Member member) {
        this.member = member;
        member.getFavoriteItems().add(this);
    }


    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        if(deleteStatus.equals(DeleteStatus.UNDELETED)){
            this.deleteStatus=DeleteStatus.DELETED;

            //상품의 관심 상품 등록수 감소
            this.getItem().minusFavoriteCount();

        }
    }

    /**
     * [비즈니스 로직]
     * recovery
     */
    public void recovery(){
        if(deleteStatus.equals(DeleteStatus.DELETED)){
            this.deleteStatus=DeleteStatus.UNDELETED;

            //상품의 관심 상품 등록수 증가
            this.getItem().plusFavoriteCount();
        }
    }





}
