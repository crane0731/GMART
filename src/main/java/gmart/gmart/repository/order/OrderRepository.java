package gmart.gmart.repository.order;

import gmart.gmart.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 레파지토리
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
