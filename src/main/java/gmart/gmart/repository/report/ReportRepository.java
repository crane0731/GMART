package gmart.gmart.repository.report;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> ,ReportRepositoryCustom{

    /**
     * 다음 조홥으로 신고 엔티티가 확인
     * @param reporter 신고자
     * @param reportedMember 피신고자
     * @param item 상품
     * @return Boolean
     */
    Boolean existsByReporterAndReportedMemberAndItem(Member reporter, Member reportedMember, Item item);
}
