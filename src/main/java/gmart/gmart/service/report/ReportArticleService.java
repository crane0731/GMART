package gmart.gmart.service.report;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.ReportArticle;
import gmart.gmart.dto.reportarticle.CreateReportArticleRequestDto;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.ReportArticleRepository;
import gmart.gmart.service.ArticleService;
import gmart.gmart.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 신고 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportArticleService {

    private final ArticleService articleService; //게시글 서비스
    private final MemberService memberService; //회원 서비스

    private final ReportArticleRepository reportArticleRepository; //신고 게시글 레파지토리


    /**
     * [서비스 로직]
     * 게시글 신고 등록
     * @param articleId 게시글 아이디
     * @param requestDto 게시글 신고 요청 DTO
     */
    @Transactional
    public void report(Long articleId, CreateReportArticleRequestDto requestDto) {

        //로그인한 회원 조회 (신고자)
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회(신고 대상)
        Article article = articleService.findById(articleId);

        //신고 당한 회원 조회
        Member reportedMember = article.getMember();

        //만약 신고자와 신고 당한 회원이 같은 회원 이라면 신고 불가(예외 던짐)
        validateSelfReport(member, reportedMember);

        //게시글 신고 로직 진행
        processReportArticle(requestDto, member, article,reportedMember);
    }

    /**
     * [저장]
     * @param reportArticle 게시글 신고 객체
     */
    public void save(ReportArticle reportArticle) {
        reportArticleRepository.save(reportArticle);
    }

    /**
     * [삭제]
     * @param reportArticle 게시글 신고 객체
     */
    public void delete(ReportArticle reportArticle) {
        reportArticleRepository.delete(reportArticle);
    }

    /**
     * [조회]
     * ID(PK)값으로 조회
     * @param id 게시글 신고 아이디
     * @return ReportArticle 엔티티 객체
     */
    public ReportArticle findById(Long id) {
        return reportArticleRepository.findById(id).orElseThrow(()->new ArticleCustomException(ErrorMessage.NOT_FOUND_REPORT_ARTICLE));
    }

    /**
     * [조회]
     * 게시글과 신고 상태가 REJECT가 아닌 게시글 신고 엔티티가 존재하는지 확인
     * @param article 게시글
     * @return boolean
     */
    public boolean existsUnrejectedReportByArticle(Article article) {
        return reportArticleRepository.existsUnrejectedReportByArticle(article);
    }

    //==게시글 신고 로직==//
    private void processReportArticle(CreateReportArticleRequestDto requestDto, Member member, Article article,Member reportedMember) {


        //게시글 신고 객체 생성 + 양방향 연관관계 세팅
        ReportArticle reportArticle = ReportArticle.createEntity(member, article, requestDto.getReason());

        //신고당한 회원의  신고 당한 수 증가
        reportedMember.plusReportedCount();

        //저장
        save(reportArticle);
    }

    //==신고자와 신고당한 회원이 같은 회원인지 확인하는 로직==//
    private void validateSelfReport(Member member, Member reportedMember) {
        if(member.getId().equals(reportedMember.getId())) {
            throw new ArticleCustomException(ErrorMessage.SELF_REPORT_NOT_ALLOWED);
        }
    }


}
