package gmart.gmart.dto.reportarticle;


import gmart.gmart.domain.enums.ReportStatus;
import gmart.gmart.dto.enums.CreateDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 게시글 리스트를 조회하기위한 조건을 담은 DTO
 */
@Getter
@Setter
public class SearchReportArticleCondDto {

    private ReportStatus reportStatus;
    private CreateDateSortType createDateSortType; //최신순, 과거순 ->[CREATE_DATE_ASC, CREATE_DATE_DESC]

}
