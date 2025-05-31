package gmart.gmart.repository.reportarticle;

import gmart.gmart.domain.ReportArticle;
import gmart.gmart.dto.reportarticle.SearchReportArticleCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL 을 사용하기 위한 커스텀 게시글 신고 레파지토리 인터페이스
 */
public interface ReportArticleRepositoryCustom {

    Page<ReportArticle> findAllByCond(SearchReportArticleCondDto cond, Pageable pageable);


}
