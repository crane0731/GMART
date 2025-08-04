package gmart.gmart.repository.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QMember;
import gmart.gmart.dto.enums.MemberSortType;
import gmart.gmart.dto.enums.SortDirection;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 커스텀 회원 레파지토리 구현체 클래스
 */
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Page<Member> findAllByCond(SearchMemberListDto cond, Pageable pageable) {

        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (cond.getNickname() != null) {
            builder.and(member.nickname.likeIgnoreCase("%" + cond.getNickname() + "%"));
        }

        if (cond.getLoginId() != null) {
            builder.and(member.loginId.likeIgnoreCase("%" + cond.getLoginId() + "%"));
        }


        // 정렬 조건
        Order direction = cond.getSortDirection() == SortDirection.ASC ? Order.ASC : Order.DESC;
        OrderSpecifier<?> orderSpecifier = null;

        if (cond.getSortType() != null) {
            switch (cond.getSortType()) {
                case REPORTED:
                    orderSpecifier = new OrderSpecifier<>(direction, member.reportedCount); // 신고당한 횟수
                    break;
                case SUSPENSION:
                    orderSpecifier = new OrderSpecifier<>(direction, member.suspensionCount); // 제재 횟수
                    break;
                case TOTAL_SPENT:
                    orderSpecifier = new OrderSpecifier<>(direction, member.totalSpent); // 총 결제액
                    break;

            }
        }

        List<Member> content = query.select(member)
                .from(member)
                .where(builder)
                .orderBy(orderSpecifier != null ? orderSpecifier : new OrderSpecifier<>(Order.ASC, member.createdDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(member.count())
                .from(member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);


    }


}
