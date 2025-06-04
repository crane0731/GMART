package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원이 관심 있어하는 건담
 * -Member 에서 생성, 삭제 생명주기를 관리함
 */
@Entity
@Table(name = "favorite_gundam")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteGundam extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("관심 건담 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favoirte_gundam_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("건담 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gundam_id")
    private Gundam gundam;

    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param gundam 건담 엔티티
     * @return FavoriteGundam 관심 건담 엔티티
     */
    public static FavoriteGundam create(Member member, Gundam gundam) {
        FavoriteGundam favoriteGundam = new FavoriteGundam();

        favoriteGundam.setMember(member);
        favoriteGundam.gundam = gundam;
        return favoriteGundam;
    }

    /**
     * [연관관계 편의 메서드]
     * FavoriteGundam - Member
     * @param member 회원 엔티티
     */
    private void setMember(Member member) {
        this.member = member;
        member.getFavoriteGundams().add(this);
    }

}
