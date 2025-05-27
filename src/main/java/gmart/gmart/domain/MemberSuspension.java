package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.MemberActiveStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 회원 제재 내역
 */
@Entity
@Table(name = "member_suspension")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSuspension extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_suspension_id")
    private Long id;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("정지 사유")
    @Column(name = "reason")
    private String reason;

    @Comment("정지 시작 날짜")
    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Comment("정지 해체 예정 날짜")
    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Comment("현재 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "active_status")
    private MemberActiveStatus memberActiveStatus;


    /**
     * 생성메서드
     */
    public static MemberSuspension createEntity(Member member, String reason, Long day) {

        MemberSuspension memberSuspension = new MemberSuspension();

        memberSuspension.member = member;
        memberSuspension.reason = reason;
        memberSuspension.startAt = LocalDateTime.now();
        memberSuspension.endAt = LocalDateTime.now().plusDays(day);
        memberSuspension.memberActiveStatus = MemberActiveStatus.ACTIVE;
        return memberSuspension;
    }

    /**
     * 현재 상태 변경 ACTIVE->RELEASE
     */
    public void releaseMemberActiveStatus(){
        this.memberActiveStatus=MemberActiveStatus.RELEASE;
    }

}
