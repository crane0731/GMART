package gmart.gmart.repository;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.LikeArticle;
import gmart.gmart.domain.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 게시글 좋아요 레파지토리
 */
public interface LikeArticleRepository extends JpaRepository<LikeArticle,Long> {

    /**
     * 이미 게시글에 좋아요를 누른 회원인지 확인하기 위한 쿼리
     * @param member 회원
     * @param article 게시글
     * @return Boolean
     */
    @Query("SELECT CASE WHEN COUNT(la) > 0 THEN true ELSE false END " +
            "FROM LikeArticle la " +
            "WHERE la.member=:member and la.article=:article")
    boolean existsByMemberAndArticle(@Param("member") Member member, @Param("article") Article article);

}
