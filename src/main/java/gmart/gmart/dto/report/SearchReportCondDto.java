package gmart.gmart.dto.report;

import gmart.gmart.domain.enums.ReportStatus;
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
    private CreatedDateSortType createdDateSortType; //최신순, 오래된순


    /**
     * [생성 메서드]
     * @param status 신고 처리 상태
     * @param createdDateSortType 최신순, 오래된순
     * @return SearchReportCondDto
     */
    public static SearchReportCondDto create(ReportStatus status,   CreatedDateSortType createdDateSortType) {
        SearchReportCondDto searchReportCondDto = new SearchReportCondDto();
        searchReportCondDto.setStatus(status);
        searchReportCondDto.setCreatedDateSortType(createdDateSortType);
        return searchReportCondDto;
    }

}
