package gmart.gmart.repository.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.QOrder;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 커스텀 주문 레파지토리 구현체
 */
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Order> findAllByCond(Member seller,OrderStatus orderStatus, Pageable pageable) {

        QOrder order = QOrder.order;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(order.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(seller != null) {
            builder.and(order.seller.eq(seller));
        }

        if (orderStatus != null) {
            builder.and(order.orderStatus.eq(orderStatus));
        }

        List<Order> content = query.select(order)
                .from(order)
                .join(order.seller).fetchJoin()
                .join(order.buyer).fetchJoin()
                .join(order.item).fetchJoin()
                .leftJoin(order.delivery).fetchJoin()
                .where(builder)
                .orderBy(order.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(order.count())
                .from(order)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }


}
