package gmart.gmart.domain.log;

import gmart.gmart.command.CreateGMoneyLogCommand;
import gmart.gmart.command.CreateGPointLogCommand;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.GPointDeltaType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 건 포인트 로그
 */

@Entity
@Table(name = "g_point_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPointLog extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_point_log_id")
    private Long id;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Comment("주문 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id" , nullable = false)
    private Order order;

    @Comment("건 포인트 변화 타입")
    @Column(name = "g_point_delta_type")
    @Enumerated(EnumType.STRING)
    private GPointDeltaType gPointDeltaType;

    @Comment("건포인트 사용 내역")
    @Column(name = "descrption")
    private String description;

    @Comment("거래 건포인트")
    @Column(name = "delta_g_point", nullable = false)
    private Long deltaGPoint;

    @Comment("거래 전 건포인트")
    @Column(name = "before_g_point", nullable = false)
    private Long beforeGPoint;

    @Comment("거래 후 건포인트")
    @Column(name = "after_g_point", nullable = false)
    private Long afterGPoint;


    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param command 생성 커맨드
     * @return GMoneyLog 건포인트 거래 로그 엔티티
     */
    public static GPointLog create(CreateGPointLogCommand command) {

        GPointLog gPointLog = new GPointLog();
        gPointLog.member = command.getMember();
        gPointLog.order = command.getOrder();
        gPointLog.gPointDeltaType = command.getGPointDeltaType();
        gPointLog.description = command.getDescription();
        gPointLog.deltaGPoint = command.getDeltaGPoint();
        gPointLog.beforeGPoint = command.getBeforeGPoint();
        gPointLog.afterGPoint = command.getAfterGPoint();
        gPointLog.deleteStatus=DeleteStatus.UNDELETED;
        return gPointLog;
    }
}
