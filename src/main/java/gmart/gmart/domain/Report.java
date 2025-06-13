package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.ReportStatus;
import gmart.gmart.domain.enums.ReporterRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 신고 테이블
 */
@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("신고 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
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

    @org.hibernate.annotations.Comment("신고자 역할")
    @Enumerated(EnumType.STRING)
    @Column(name = "reporter_role")
    private ReporterRole reporterRole;


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
    public static Report create(ReporterRole reporterRole,Member reporter, Member reportedMember, Item item, String reason) {
        Report reportItem = new Report();
        reportItem.reporter = reporter;
        reportItem.reportedMember = reportedMember;
        reportItem.item = item;
        reportItem.reason = reason;
        reportItem.reportStatus=ReportStatus.WAITING;
        reportItem.reporterRole=reporterRole;
        reportItem.deleteStatus=DeleteStatus.UNDELETED;

        return reportItem;

    }

    /**
     * [비즈니스 로직]
     * 관리자가 신고 수락
     */
    public void accept() {
        if(this.reportStatus!=ReportStatus.ACCEPTED){
            this.reportStatus=ReportStatus.ACCEPTED;
        }
    }

    /**
     * [비즈니스 로직]
     * 관리자가 신고 거절
     * 만약 신고자가 구매자면 -> 상품의 신고수 내림 + 피신고자의 신고수 내림
     * 만약 신고자가 판매자면 -> 피신고자의 신고수만 내림
     */
    public void reject() {
        if( this.reportStatus!=ReportStatus.REJECTED){
            this.reportStatus=ReportStatus.REJECTED;

            this.reportedMember.minusReportedCount();

            if (this.reporterRole==ReporterRole.BUYER) {
                this.item.minusReportCount();
            }
        }
    }

}
