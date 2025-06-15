package gmart.gmart.repository.adminmessage;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.AdminMessage;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QAdminMessage;
import gmart.gmart.domain.QMember;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import gmart.gmart.dto.enums.CreatedDateSortType;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class AdminMessageRepositoryImpl implements AdminMessageRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public AdminMessageRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 검잭 조건 에따라 특정 회원의 관리자 메시지 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<AdminMessage> 페이징된 관리자 메시지 엔티티 리스트
     */
    @Override
    public Page<AdminMessage> findAllByCond(Member member, SearchAdminMessageCondDto cond, Pageable pageable) {

        QAdminMessage adminMessage = QAdminMessage.adminMessage;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(adminMessage.member.eq(member));
        builder.and(adminMessage.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(cond.getContent() != null && !cond.getContent().isBlank()) {
            builder.and(adminMessage.content.containsIgnoreCase(cond.getContent()));
        }

        OrderSpecifier<LocalDateTime> order =
                cond.getCreatedDateSortType() == CreatedDateSortType.CREATE_DATE_ASC ? adminMessage.createdDate.asc() : adminMessage.createdDate.desc();

        List<AdminMessage> content = query
                .select(adminMessage)
                .from(adminMessage)
                .join(adminMessage.member).fetchJoin()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = query.select(adminMessage.count())
                .from(adminMessage)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
