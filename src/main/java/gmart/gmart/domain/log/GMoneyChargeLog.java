package gmart.gmart.domain.log;


import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ChargeType;
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
    @Column(name = "before_charge_money")
    private Long beforeChargeMoney;

    @Comment("충전 후 금액")
    @Column(name = "after_charge_money")
    private Long afterChargeMoney;

}
