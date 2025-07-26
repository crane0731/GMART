package gmart.gmart.repository.order;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 주문 레파지토리
 */
public interface OrderRepositoryCustom {

    Page<Order> findAllByCondAndSeller(Member seller , OrderStatus orderStatus, Pageable pageable);

    Page<Order> findAllByCondAndBuyer(Member buyer , OrderStatus orderStatus, Pageable pageable);

}
