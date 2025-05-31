package gmart.gmart.repository.article;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Article;
import gmart.gmart.domain.QArticle;
import gmart.gmart.dto.article.SearchArticleCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 커스텀 게시글 레파지토리 구현체 클래스
 */
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ArticleRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 조건에 따라 게시글 리스트를 검색하는 동적 쿼리 메서드
     * @param cond 검색 조건
     * @param pageable 페이징 처리를 위한 객체
     * @return 문의 엔티티 리스트
     */
    @Override
    public Page<Article> findAllByCond(SearchArticleCondDto cond, Pageable pageable) {

        QArticle article = QArticle.article;

        BooleanBuilder builder = new BooleanBuilder();

        //게시글 제목으로 검색
        if (cond.getTitle() != null && !cond.getTitle().isBlank()) {
            builder.and(article.title.like("%" + cond.getTitle() + "%"));
        }

        //게시글 작성자 닉네임으로 검색
        if (cond.getNickname() != null && !cond.getNickname().isBlank()) {
            builder.and(article.member.nickname.like("%" + cond.getNickname() + "%"));
        }

        //정렬 타입 설정
        OrderSpecifier<?> sortOrder = null;

        if (cond.getSortType() != null) {
            switch (cond.getSortType()) {
                case COMMENT_HIGH -> sortOrder = article.commentCount.desc();
                case COMMENT_LOW -> sortOrder = article.commentCount.asc();
                case LIKE_HIGH -> sortOrder = article.likeCount.desc();
                case LIKE_LOW -> sortOrder = article.likeCount.asc();
                case VIEW_HIGH -> sortOrder = article.viewCount.desc();
                case VIEW_LOW -> sortOrder = article.viewCount.asc();
                case CREATE_DATE_ASC -> sortOrder =article.createdDate.asc();
                case CREATE_DATE_DESC -> sortOrder = article.createdDate.desc();
            }
        }

        //실제 페이지 데이터 조회
        List<Article> content = query
                .select(article)
                .from(article)
                .where(builder)
                .orderBy(sortOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 개수 조회(페이징을 위해 필요)
        Long total = query.select(article.count())
                .from(article)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}