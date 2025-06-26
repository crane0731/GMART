package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GundamCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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
    @Column(name = "favorite_gundam_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("건담 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gundam_id")
    private Gundam gundam;

    @Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

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
        favoriteGundam.deleteStatus=DeleteStatus.UNDELETED;
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

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        //이미 삭제 상태인지 확인
        validateDeleted();

        //삭제 처리
        this.deleteStatus=DeleteStatus.DELETED;
    }

    //==이미 삭제 상태인지 학인하는 로직==//
    private void validateDeleted() {
        if(this.deleteStatus.equals(DeleteStatus.DELETED)){
            throw new GundamCustomException(ErrorMessage.ALREADY_DELETE);
        }
    }

}
