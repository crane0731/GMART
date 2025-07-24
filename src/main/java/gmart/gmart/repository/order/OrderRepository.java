package gmart.gmart.repository.order;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.OrderStatus;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 주문 레파지토리
 */
public interface OrderRepository extends JpaRepository<Order, Long>,OrderRepositoryCustom {

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.buyer=:buyer and o.item=:item and " +
            "(o.orderStatus=:reservedStatus or o.orderStatus=:cancelRequestStatus)")
    Optional<Order>findByBuyerAndItem(@Param("buyer") Member buyer, @Param("item") Item item, @Param("reservedStatus") OrderStatus reservedStatus, @Param("cancelRequestStatus") OrderStatus cancelRequestStatus);
}
