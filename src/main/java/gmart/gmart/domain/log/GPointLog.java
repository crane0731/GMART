package gmart.gmart.domain.log;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
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
public class GPointLog extends BaseAuditingEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_point_log_id")
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


}
