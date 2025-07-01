package gmart.gmart.domain.log;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.enums.DeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 건포인트 충전 로그
 */
@Entity
@Table(name = "g_point_charge_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPointChargeLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_point_charge_log_id")
    private Long id;

    @Comment("회원 아이디")
    @Column(name = "member_id")
    private Long memberId;

    @Comment("충전 방식")
    @Column(name = "charge_type")
    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @Comment("충전 금액")
    @Column(name = "charge_amount")
    private Long chargeAmount;

    @Comment("충전 전 금액")
    @Column(name = "before_charge_point")
    private Long beforeChargePoint;

    @Comment("충전 후 금액")
    @Column(name = "after_charge_point")
    private Long afterChargePoint;

    @Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;


    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param chargeType 충전 타입
     * @param chargePoint 충전 포인트
     * @param beforeChargePoint 충전 전 포인트
     * @param afterChargePoint 충전 후 포인트
     * @return GPointChargeLog 건포인트 충전 로그 엔티티
     */
    public static GPointChargeLog create(Member member , ChargeType chargeType , Long chargePoint , Long beforeChargePoint , Long afterChargePoint) {
        GPointChargeLog log = new GPointChargeLog();
        log.memberId = member.getId();
        log.chargeType = chargeType;
        log.chargeAmount = chargePoint;
        log.beforeChargePoint = beforeChargePoint;
        log.afterChargePoint = afterChargePoint;
        log.deleteStatus = DeleteStatus.UNDELETED;
        return log;
    }
}
