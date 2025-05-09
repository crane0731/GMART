package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.GundamGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 회원이 선호하는 건담 등급
 */

@Entity
@Table(name = "member_gundam_grade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGundamGrade extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_gundam_grade_id")
    private Long id;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Comment("건담 등급")
    @Enumerated(EnumType.STRING)
    @Column(name = "gundam_grade", nullable = false)
    private GundamGrade gundamGrade;

    /**
     * setter
     * @param member
     */
    protected void setMember(Member member) {
        this.member = member;
    }


    /**
     * 생성 메서드
     * @param gundamGrade
     * @return
     */
    public static MemberGundamGrade createEntity(String gundamGrade) {
        MemberGundamGrade memberGundamGrade = new MemberGundamGrade();
        memberGundamGrade.gundamGrade= GundamGrade.valueOf(gundamGrade);
        return memberGundamGrade;

    }


}
