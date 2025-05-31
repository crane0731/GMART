package gmart.gmart.repository.article;

import gmart.gmart.domain.Article;
import gmart.gmart.dto.article.SearchArticleCondDto;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL 을 사용하기 위한 커스텀 게시글 레파지토리 인터페이스
 */
public interface ArticleRepositoryCustom {

    Page<Article> findAllByCond(SearchArticleCondDto cond, Pageable pageable);

}
