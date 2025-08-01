package gmart.gmart.domain.log;


import gmart.gmart.domain.Member;
import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GMoneyCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 건머니 충전 로그
 */

@Entity
@Table(name = "g_money_charge_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GMoneyChargeLog extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_money_charge_log_id")
    private Long id;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Comment("충전 방식")
    @Column(name = "charge_type")
    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @Comment("충전 금액")
    @Column(name = "charge_amount")
    private Long chargeAmount;

    @Comment("충전 전 금액")
    @Column(name = "before_charge_money")
    private Long beforeChargeMoney;

    @Comment("충전 후 금액")
    @Column(name = "after_charge_money")
    private Long afterChargeMoney;

    @Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param member 회원 엔티티
     * @param beforeChargeMoney 충전 전 금액
     * @param chargeMoney 충전한 금액
     * @param chargeType 충전 타입
     * @return GMoneyChargeLog 건머니 충전 로그 엔티티
     */
    public static GMoneyChargeLog create(Member member, Long beforeChargeMoney, Long chargeMoney, ChargeType chargeType) {
        GMoneyChargeLog log = new GMoneyChargeLog();
        log.member = member;

        log.chargeType = chargeType;

        log.chargeAmount = chargeMoney;
        log.beforeChargeMoney = beforeChargeMoney;

        log.afterChargeMoney = getAfterChargeMoney(beforeChargeMoney, chargeMoney, chargeType);

        log.deleteStatus=DeleteStatus.UNDELETED;

        return log;
    }

    /**
     * [비즈니스 로직]
     * 충전 후 금액 구하기
     * 충전 : 충전 전 금액 + 충전한 금액
     * 환불 : 환불 전 금액 - 환불한 금액
     * @param beforeChargeMoney 충전 전 금액
     * @param chargeMoney 충전한 금액
     * @return Long 충전 후 금액
     */
    private static long getAfterChargeMoney(Long beforeChargeMoney, Long chargeMoney,ChargeType chargeType) {
        if(chargeType.equals(ChargeType.REFUND)){

            if(beforeChargeMoney-chargeMoney<0){
                throw new GMoneyCustomException(ErrorMessage.NOT_ENOUGH_GMONEY_TO_REFUND);
            }
            return beforeChargeMoney - chargeMoney;
        }
        else {
            return beforeChargeMoney + chargeMoney;
        }
    }

}
