package gmart.gmart.repository.reportarticle;

import gmart.gmart.domain.Article;
import gmart.gmart.domain.ReportArticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 게시글 신고 레파지토리
 */
public interface ReportArticleRepository extends JpaRepository<ReportArticle, Long> ,ReportArticleRepositoryCustom{


    @Query("SELECT CASE  WHEN COUNT(ra) > 0 THEN true ELSE false END " +
            "FROM ReportArticle ra " +
            "WHERE ra.article = :article AND ra.status != 'REJECTED'")
    boolean existsUnrejectedReportByArticle(@Param("article") Article article);

}
