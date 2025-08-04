package gmart.gmart.repository.order;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.OrderStatus;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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


    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.orderStatus = :orderStatus AND  o.deleteStatus = :deleteStatus")
    Page<Order> findAllByOrderStatus(@Param("orderStatus") OrderStatus orderStatus, @Param("deleteStatus")DeleteStatus deleteStatus, Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderStatus = :status AND o.deleteStatus = :deleteStatus")
    Long findTotalGMoneyByOrderStatus(@Param("status") OrderStatus status, @Param("deleteStatus") DeleteStatus deleteStatus);

    @Query("SELECT SUM(o.paidPrice) FROM Order o WHERE o.orderStatus = :status AND o.deleteStatus = :deleteStatus")
    Long findTotalPaidGMoneyByOrderStatus(@Param("status") OrderStatus status, @Param("deleteStatus") DeleteStatus deleteStatus);

    @Query("SELECT SUM(o.usedPoint) FROM Order o WHERE o.orderStatus = :status AND o.deleteStatus = :deleteStatus")
    Long findTotalUsedGPointByOrderStatus(@Param("status") OrderStatus status, @Param("deleteStatus") DeleteStatus deleteStatus);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status AND o.deleteStatus = :deleteStatus")
    Long countItemsByOrderStatus(@Param("status") OrderStatus status, @Param("deleteStatus") DeleteStatus deleteStatus);

}
