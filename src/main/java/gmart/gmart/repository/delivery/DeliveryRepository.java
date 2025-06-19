package gmart.gmart.repository.delivery;

import gmart.gmart.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 배송 레파지토리
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
