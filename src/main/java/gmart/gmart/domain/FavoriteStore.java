package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 관심 상점
 */
@Entity
@Table(
        name = "favorite_store",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_favorite_store_member_store",
                        columnNames = {"member_id", "store_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteStore extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("관심 상점 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_store_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("상점 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return  FavoriteStore 관심 상점 엔티티
     */
    public static FavoriteStore create(Member member, Store store) {
        FavoriteStore favoriteStore = new FavoriteStore();
        favoriteStore.setMember(member);
        favoriteStore.store = store;
        return favoriteStore;
    }


    /**
     * [연관 관계 편의 메서드]
     * @param member 회원 엔티티
     */
    private void setMember(Member member) {
        this.member = member;
        member.getFavoriteStores().add(this);
    }
}
