package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 신고
 */
@Entity
@Table(name = "report_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportItem extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("상품 신고 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_item_id")
    private Long id;

    @org.hibernate.annotations.Comment("신고자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @org.hibernate.annotations.Comment("신고받은 회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;


    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @org.hibernate.annotations.Comment("신고 사유")
    @Column(name = "reason", nullable = false)
    private String reason;

    @org.hibernate.annotations.Comment("신고 처리 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "report_status",nullable = false)
    private ReportStatus reportStatus;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;


    /**
     * [생성 메서드]
     * @param reporter 신고자
     * @param reportedMember 피신고자
     * @param item 신고하는 상품
     * @param reason 신고사유
     * @return ReportItem 상품 신고 엔티티
     */
    public static ReportItem create(Member reporter, Member reportedMember, Item item, String reason) {
        ReportItem reportItem = new ReportItem();
        reportItem.reporter = reporter;
        reportItem.reportedMember = reportedMember;
        reportItem.item = item;
        reportItem.reason = reason;
        reportItem.reportStatus=ReportStatus.WAITING;
        reportItem.deleteStatus=DeleteStatus.UNDELETED;
        return reportItem;

    }

}
