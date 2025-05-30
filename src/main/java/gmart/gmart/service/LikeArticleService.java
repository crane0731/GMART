package gmart.gmart.service;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.LikeArticle;
import gmart.gmart.domain.Member;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 좋아요 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeArticleService {

    private final LikeArticleRepository likeArticleRepository; //게시글 좋아요 레파지토리

    /**
     * [조회]
     * 회원과 게시글로 좋아요 게시글을 조회는 메서드
     * @param member 회원
     * @param article 게시글
     * @return LikeArticle 엔티티
     */
    public LikeArticle findLikeArticleByMemberAndArticle(Member member, Article article) {
        return likeArticleRepository.findByMemberAndArticle(member, article).orElseThrow(() -> new ArticleCustomException(ErrorMessage.NOT_FOUND_LIKE_ARTICLE));
    }

    /**
     * [조회]
     * 회원과 게시글로 좋아여 게시글이 존재하는지 확인하는 메서드
     * @param member 회원
     * @param article 게시글
     * @return boolean
     */
    public boolean existsByMemberAndArticle(Member member, Article article) {
        return likeArticleRepository.existsByMemberAndArticle(member, article);
    }

    /**
     * [저장]
     * 좋아요 게시글 저장 메서드
     * @param likeArticle 좋아요 게시글
     */
    public void save(LikeArticle likeArticle) {
        likeArticleRepository.save(likeArticle);
    }

    /**
     * [삭제]
     * 좋아요 게시글 삭제 메서드
      * @param likeArticle 좋아요 게시글
     */
    public void delete(LikeArticle likeArticle) {
        likeArticleRepository.delete(likeArticle);
    }




}
