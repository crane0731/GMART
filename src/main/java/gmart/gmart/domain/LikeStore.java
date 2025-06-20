package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상점 좋아요
 */
@Entity
@Table(
        name = "like_store",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_like_store_member_store",
                        columnNames = {"member_id", "store_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeStore extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("상점 좋아요 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_store_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("상점 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return LikeStore 상점 좋아요 엔티티
     */
    public static LikeStore create(Member member, Store store) {
        LikeStore likeStore = new LikeStore();
        likeStore.setMember(member);
        likeStore.store = store;
        likeStore.deleteStatus = DeleteStatus.UNDELETED;
        return likeStore;
    }

    /**
     * [연관 관계 편의 메서드]
     * @param member 회원 엔티티
     */
    private void setMember(Member member) {
        this.member = member;
        member.getLikeStores().add(this);
    }

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete() {

        if(deleteStatus == DeleteStatus.UNDELETED) {
            this.deleteStatus=DeleteStatus.DELETED;

        }
    }

    /**
     * [비즈니스 로직]
     * RECOVERY
     */
    public void recovery(){

        if (deleteStatus == DeleteStatus.DELETED) {
            this.deleteStatus=DeleteStatus.UNDELETED;

            this.store.plusLikedCount();
        }
    }

}
