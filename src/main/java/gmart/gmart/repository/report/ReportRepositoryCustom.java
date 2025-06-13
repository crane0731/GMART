package gmart.gmart.repository.report;

import gmart.gmart.domain.Report;
import gmart.gmart.dto.report.SearchReportCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 신고 레파지토리
 */
public interface ReportRepositoryCustom {


    /**
     * 검색 조건에 맞춰 신고 리스트 목록을 조회
     * @param cond 검색 조건 DTO
     * @return Page<Report> 페이징된 신고 엔티티
     */
    Page<Report> findAllByCond(SearchReportCondDto cond, Pageable pageable);
}
