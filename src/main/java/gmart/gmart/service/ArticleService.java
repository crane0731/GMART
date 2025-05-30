package gmart.gmart.service;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.LikeArticle;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.UploadedImage;
import gmart.gmart.dto.article.CreateArticleRequestDto;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.ArticleRepository;
import gmart.gmart.repository.LikeArticleRepository;
import gmart.gmart.service.image.UploadMemberProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final MemberService memberService; //회원 서비스
    private final UploadMemberProfileImageService uploadImageService; //업로드 이미지 서비스

    private final ArticleRepository articleRepository; //게시글 레파지토리
    private final LikeArticleRepository likeArticleRepository; //게시글 좋아요 레파지토리

    /**
     * [서비스 로직]
     * 게시글 등록
     * @param requestDto 게시글 등록 요청 DTO
     */
    @Transactional
    public void createArticle(CreateArticleRequestDto requestDto) {

        //로그인한 회원
        Member member = memberService.findBySecurityContextHolder();

        //게시글 엔티티 생성 + 이미지 세팅
        Article article = createArticleWithArticleImage(requestDto, member);

        //게시글 엔티티 저장
        save(article);
    }

    /**
     * [서비스 로직]
     * 게시글 삭제
     * @param articleId 게시글 아이디
     */
    @Transactional
    public void deleteArticle(Long articleId) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회
        Article article = findById(articleId);

        //삭제할 게시글이 로그인한 회원이 쓴 게시글인지 확인
        article.validateOwner(member);

        //게시글 삭제
        delete(article);

    }


    /**
     * 게시글 좋아요 서비스 로직
     */
    @Transactional
    public void likeArticle(Long articleId) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회
        Article article = findById(articleId);

        //회원 + 게시글 조합으로 객체가 이미 게시글 좋아요 DB에 존재하는지 확인
        existsLikeArticle(member, article);

        //게시글 좋아요 객체 생성
        LikeArticle likeArticle = LikeArticle.createEntity(member, article);

        //저장
        likeArticleRepository.save(likeArticle);

    }




    /**
     *  게시글 수정 서비스 로직
     */



    /**
     * 게시글 단일 조회(상세) 서비스 로직
     */

    /**
     *게시글 리스트 조회 서비스 로직
     */

    /**
     * 게시글 신고 서비스 로직
     */



    /**
     * [조회]
     * 게시글 조회 메서드
     * @param articleId 게시글 아이디
     * @return Article 엔티티 객체
     */
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(()-> new ArticleCustomException(ErrorMessage.NOT_FOUND_ARTICLE));
    }

    /**
     * [저장]
     * 게시글 저장 메서드
     * @param article 게시글 엔티티 객체
     */
    public void save(Article article) {
        articleRepository.save(article);
    }

    /**
     * [삭제]
     * 게시글 삭제 메서드
     * @param article
     */
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    //==게시글 엔티티 생성 + 이미지 세팅==//
    private Article createArticleWithArticleImage(CreateArticleRequestDto requestDto, Member member) {
        //게시글 객체 생성
        Article article = Article.createEntity(member, requestDto.getTitle(), requestDto.getContent());

        List<String> imageUrls = requestDto.getImageUrls();

        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {

                //업로드 이미지 조회
                UploadedImage uploadedImage = uploadImageService.findByImageUrl(imageUrl);

                //이미지 사용 처리
                if (!uploadedImage.isUsed()) {
                    uploadedImage.usedTrue();
                }

                //게시글 이미지 객체 생성
                article.attachArticleImage(imageUrl);

            }
        }
        return article;

    }

    //==회원과 게시글로 이미 좋아요 게시글이 DB에 존재하는지 확인하는 로직==//
    private void existsLikeArticle(Member member, Article article) {
        boolean exists = likeArticleRepository.existsByMemberAndArticle(member, article);
        if (exists) {
            throw new ArticleCustomException(ErrorMessage.ALREADY_LIKE_ARTICLE);
        }
    }

}
