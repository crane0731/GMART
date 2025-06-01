package gmart.gmart.service.article;

import gmart.gmart.domain.*;
import gmart.gmart.dto.article.ArticleDetailResponseDto;
import gmart.gmart.dto.article.CreateArticleRequestDto;
import gmart.gmart.dto.article.SearchArticleCondDto;
import gmart.gmart.dto.article.UpdateArticleRequestDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.article.ArticleRepository;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.image.UploadMemberProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final LikeArticleService likeArticleService;//게시글 좋아요 서비스

    private final ArticleRepository articleRepository; //게시글 레파지토리

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
     * [서비스 로직]
     * 게시글 좋아요
     * LikeArticle 객체를 DB에 저장하고 게시글의 좋아요 수 를 증가 시키는 로직
     * @param articleId 게시글 아이디
     */
    @Transactional
    public void likeArticle(Long articleId) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회
        Article article = findById(articleId);

        //게시글 좋아요 로직 진행
        processingLike(member, article);

    }



    /**
     * [서비스 로직]
     * 게시글 좋아요 취소
     * LikeArticle 객체를 DB 에서 삭제하고 게시글의 좋아요 수 를 감소 시키는 로직
     * @param articleId 게시글 아이디
     */
    @Transactional
    public void unlikeArticle(Long articleId) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회
        Article article = findById(articleId);

        //게시글 좋아요 취소 로직 진행
        processingUnlike(member, article);


    }


    /**
     * [서비스 로직]
     * 게시글 업데이트
     * 1.로그인한 회원과 업데이트 할 게시글을 조회 하고 권한 확인
     * 2.기존의 업로드 이미지는 전부 비사용 처리 + ArticleImage DB 에서는 삭제(orphanRemoval)
     * 3.새로운 업로드 이미지는 전부 사용 처리 + ArticleImage DB 에 저장(CASCADE)
     * 4 이미지 변경은 변경감지를 통해 리스트 통째로 갈아 끼우기
     * 5.게시글을 새로운 내용, 이미지로 업데이트
     * @param articleId 게시글 아이디
     * @param requestDto 업데이트 요청 DTO
     */
    @Transactional
    public void updateArticle(Long articleId,UpdateArticleRequestDto requestDto) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //게시글 조회
        Article article = findById(articleId);

        //현재 로그인한 회원이 쓴 게시글인지 확인
        article.validateOwner(member);

        //기존의 게시글 이미지 비사용 처리
        markOldImagesAsUnused(article);

        //업데이트 + 이미지 사용 처리
        processingUpdateWithUploadImage(article,requestDto);

    }


    /**
     * [서비스 로직]
     * 게시글 단일 조회(상세)
     * @param articleId 게시글 아이디
     * @return ArticleResponseDto 응답 DTO
     */
    @Transactional
    public ArticleDetailResponseDto getArticleInfo(Long articleId) {

        //게시글 조회
        Article article = findById(articleId);

        //게시글 조회수 증가 시키기
        article.addViewCount();

        return ArticleDetailResponseDto.createDto(article);

    }

    /**
     * [서비스 로직]
     * 조건에 따라 게시글 리스트를 조회
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<ArticleDetailResponseDto> 페이징된 응답 DTO
     */
    public PagedResponseDto<ArticleDetailResponseDto> getAllArticles(SearchArticleCondDto condDto) {

        //페이징된 Article 리스트 조회
        Page<Article> page = findAllByCond(condDto);

        //게시글 리스트 응답 DTO 생성
        List<ArticleDetailResponseDto> content = createArticleDetailResponseDto(page);

        //페이징 응답 DTO 생성 + 반환
        return createArticleDetailResponseDtoPagedResponseDto(content, page);

    }

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
        boolean exists = likeArticleService.existsByMemberAndArticle(member, article);
        if (exists) {
            throw new ArticleCustomException(ErrorMessage.ALREADY_LIKE_ARTICLE);
        }
    }

    //==좋아요 취소 로직==//
    private void processingUnlike(Member member, Article article) {
        //게시글 좋아요 찾기
        LikeArticle likeArticle = likeArticleService.findLikeArticleByMemberAndArticle(member, article);

        //게시글 좋아요 취소
        article.unlike(likeArticle);

        //게시글 좋아요 삭제
        likeArticleService.delete(likeArticle);
    }

    //==게시글 좋아요 로직==//
    private void processingLike(Member member, Article article) {
        //회원 + 게시글 조합으로 객체가 이미 게시글 좋아요 DB에 존재하는지 확인
        existsLikeArticle(member, article);

        //게시글 좋아요 객체 생성
        LikeArticle likeArticle = LikeArticle.createEntity(member, article);

        //저장
        likeArticleService.save(likeArticle);
    }

    //==업로드 이미지 사용처리 로직==//
    private void markImagesAsUsed(List<String> imageUrls) {
        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {

                //업로드 이미지 조회
                UploadedImage uploadedImage = uploadImageService.findByImageUrl(imageUrl);

                //이미지 사용 처리
                if (!uploadedImage.isUsed()) {
                    uploadedImage.usedTrue();
                }
            }
        }
    }

    //==ArticleImages 게시글 이미지 생성 메서드==//
    private List<ArticleImage> getArticleImages(Article article,List<String> imageUrls) {

        return imageUrls.stream()
                .map(ArticleImage::createEntity)
                .toList();
    }

    //==기존의 게시글 이미지 비사용 처리하는 로직==//
    private void markOldImagesAsUnused(Article article) {
        //기존의 게시글 이미지
        List<ArticleImage> oldArticleImages = article.getArticleImages();

        //기존의 게시글 이미지 비사용 처리
        for (ArticleImage oldArticleImage : oldArticleImages) {
            UploadedImage uploadedImage = uploadImageService.findByImageUrl(oldArticleImage.getImageUrl());
            uploadedImage.usedFalse();
        }
    }

    //==업데이트 + 새로운 업로드 이미지 사용 처리 로직==//
    private void processingUpdateWithUploadImage(Article article,UpdateArticleRequestDto requestDto) {

            //업로드된 이미지 사용 처리
            markImagesAsUsed(requestDto.getImageUrls());

            //게시글 이미지 생성
            List<ArticleImage> articleImages = getArticleImages(article,requestDto.getImageUrls());

            //게시글 업데이트
            article.update(requestDto.getTitle(), requestDto.getContent(), articleImages);

    }

    //==페이징 생성 메서드==//
    private Pageable createPageable() {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(0, 10);
    }

    //==검색 조건에 따라 게시글 리스트 조회 + 페이징 하는 로직==//
    private Page<Article> findAllByCond(SearchArticleCondDto condDto) {
        Pageable pageable = createPageable();

        return articleRepository.findAllByCond(condDto, pageable);
    }

    //==ArticleDetailResponseDto 리스트 생성 메서드==//
    private List<ArticleDetailResponseDto> createArticleDetailResponseDto(Page<Article> page) {
        return page.getContent().stream()
                .map(ArticleDetailResponseDto::createDto)
                .toList();
    }

    //==페이징 응답 DTO 생성 + 반환 메서드 ==//
    private PagedResponseDto<ArticleDetailResponseDto> createArticleDetailResponseDtoPagedResponseDto(List<ArticleDetailResponseDto> content, Page<Article> page) {
        return new PagedResponseDto<ArticleDetailResponseDto>(content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast());
    }

}
