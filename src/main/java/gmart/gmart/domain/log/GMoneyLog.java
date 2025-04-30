package gmart.gmart.domain.log;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 건 머니 거래 로그
 */

@Entity
@Table(name = "g_money_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GMoneyLog extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_money_log_id")
    private Long id;

    @Comment("회원 아이디")
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Comment("결제 아이디")
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Comment("주문 아이디")
    @Column(name = "order_id" , nullable = false)
    private Long orderId;

    @Comment("건머니 변화 타입")
    @Column(name = "g_money_delta_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GMoneyDeltaType gMoneydeltaType;

    @Comment("건머니 사용 내역")
    @Column(name = "descrption")
    private String description;

    @Comment("거래 건머니")
    @Column(name = "delta_g_money", nullable = false)
    private Long deltaGMoney;

    @Comment("거래 전 건머니")
    @Column(name = "before_g_money", nullable = false)
    private Long beforeGMoney;

    @Comment("거래 후 건 머니")
    @Column(name = "after_g_money", nullable = false)
    private Long afterGMoney;




}
