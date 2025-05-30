package gmart.gmart.repository;

import gmart.gmart.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 레파지토리
 */

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
