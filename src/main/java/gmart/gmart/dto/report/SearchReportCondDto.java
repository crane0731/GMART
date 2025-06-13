package gmart.gmart.dto.report;

import gmart.gmart.domain.enums.ReportStatus;
import gmart.gmart.domain.enums.ReporterRole;
import gmart.gmart.dto.enums.CreatedDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 목록 조회 검색 조건 DTO
 */
@Getter
@Setter
public class SearchReportCondDto {

    private ReportStatus status; //신고 처리 상태
    private ReporterRole reporterRole; //신고자 역할
    private CreatedDateSortType createdDateSortType; //최신순, 오래된순


    /**
     * [생성 메서드]
     * @param status 신고 처리 상태
     * @param reporterRole 신고자 역할
     * @param createdDateSortType 최신순, 오래된순
     * @return
     */
    public static SearchReportCondDto create(ReportStatus status, ReporterRole reporterRole, CreatedDateSortType createdDateSortType) {
        SearchReportCondDto searchReportCondDto = new SearchReportCondDto();
        searchReportCondDto.setStatus(status);
        searchReportCondDto.setReporterRole(reporterRole);
        searchReportCondDto.setCreatedDateSortType(createdDateSortType);
        return searchReportCondDto;
    }

}
