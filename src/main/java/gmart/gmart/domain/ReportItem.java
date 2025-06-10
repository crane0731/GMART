package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 신고
 */
@Entity
@Table(name = "report_article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportItem extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("상품 신고 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_item_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    @org.hibernate.annotations.Comment("신고 사유")
    @Column(name = "reason", nullable = false)
    private String reason;

    @org.hibernate.annotations.Comment("신고 처리 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ReportStatus status;

}
