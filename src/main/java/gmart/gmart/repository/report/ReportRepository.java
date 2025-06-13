package gmart.gmart.repository.report;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Report;
import gmart.gmart.domain.enums.ReporterRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> ,ReportRepositoryCustom{

    /**
     * 다음 조홥으로 신고 엔티티가 확인
     * @param reporter 신고자
     * @param reportedMember 피신고자
     * @param item 상품
     * @param reporterRole 신고자 역할
     * @return Boolean
     */
    Boolean existsByReporterAndReportedMemberAndItemAndReporterRole(Member reporter, Member reportedMember, Item item, ReporterRole reporterRole);
}
