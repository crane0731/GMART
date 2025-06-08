package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
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

}
