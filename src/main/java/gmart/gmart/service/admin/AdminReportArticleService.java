package gmart.gmart.service.admin;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.ReportArticle;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.reportarticle.ReportArticleDetailResponseDto;
import gmart.gmart.dto.reportarticle.ReportArticleListResponseDto;
import gmart.gmart.dto.reportarticle.SearchReportArticleCondDto;
import gmart.gmart.service.ArticleService;
import gmart.gmart.service.MemberService;
import gmart.gmart.service.report.ReportArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 게시글 신고 관리 서비스
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportArticleService {

    private final MemberService memberService; //회원 서비스
    private final ArticleService articleService;//게시글 서비스
    private final ReportArticleService reportArticleService;//게시글 신고 서비스

    /**
     * [서비스 로직]
     * 게시글 신고 수락
     * @param reportArticleId 게시글 신고 아이디
     */
    @Transactional
    public void acceptReport(Long reportArticleId){

        //게시글 신고 객체 조회
        ReportArticle reportArticle = reportArticleService.findById(reportArticleId);

        //게시글 신고 수락 처리
        reportArticle.acceptReport();
    }

    /**
     *[서비스 로직]
     *게시글 신고 거절
     *회원 신고당한 횟수 감소
     * @param reportArticleId 게시글 신고 아이디
     */
    @Transactional
    public void rejectReport(Long reportArticleId){

        //게시글 신고 객체 조회
        ReportArticle reportArticle = reportArticleService.findById(reportArticleId);

        //게시글 신고 거절 처리
        reportArticle.rejectReport();

        //신고 거절 처리 후 게시글의 신고 상태 확인 및 변경
        handleArticleReportStatusAfterRejection(reportArticle.getArticle());

    }

    /**
     * [서비스 로직]
     * 게시글 신고 단일 상세 조회
     * @param reportArticleId 게시글 신고 아이디
     * @return ReportArticleDetailResponseDto 응답 DTO
     */
    public ReportArticleDetailResponseDto getReportArticleDetail(Long reportArticleId){

        //게시글 신고 조회
        ReportArticle reportArticle = reportArticleService.findById(reportArticleId);

        //응답 DTO 생성 + 반환
        return ReportArticleDetailResponseDto.createDto(reportArticle);
    }

    /**
     * [서비스 로직]
     * 조건에 따라 게시글 모든 신고 리스트 조회(+페이징)
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<ReportArticleListResponseDto> 응답 DTO
     */
    public PagedResponseDto<ReportArticleListResponseDto> getAllReportArticle(SearchReportArticleCondDto condDto){

        //페이징 생성
        Pageable pageable = createPageable();

        //게시글 신고 조회 + 페이징
        Page<ReportArticle> page = reportArticleService.findAllByCond(condDto, pageable);

        //응답 객체 리스트 생성
        List<ReportArticleListResponseDto> content = createReportArticleListResponseDtos(page);

        return createPagedResponseDto(content,page);
    }



    //==ReportArticleListResponseDto 객체 리스트 생성 메서드==//
    private List<ReportArticleListResponseDto> createReportArticleListResponseDtos(Page<ReportArticle> page) {
        List<ReportArticleListResponseDto> content = page.getContent()
                .stream()
                .map(ReportArticleListResponseDto::createDto)
                .toList();
        return content;
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<ReportArticleListResponseDto> createPagedResponseDto(List<ReportArticleListResponseDto> content, Page<ReportArticle> page) {
        return new PagedResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }


    //==페이징 생성 메서드==//
    private Pageable createPageable() {
        Pageable pageable = PageRequest.of(0, 10); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==신고 거절 처리 후 게시글의 신고 상태 확인 및 변경==//
    private void handleArticleReportStatusAfterRejection(Article article) {

        //게시글에 대해 다른 신고 접수되거나 접수 대기중인 게시글 신고가 있는지 확인
        boolean hasOtherActiveReports = reportArticleService.existsUnrejectedReportByArticle(article);

        //만약 없다면 게시글 신고 상태를 '신고안됨' 상태로 변경
        if(!hasOtherActiveReports){
            article.changeReportedStatusToNotReported();
        }
    }

}
