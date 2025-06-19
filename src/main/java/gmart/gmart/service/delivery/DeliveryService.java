package gmart.gmart.service.delivery;

import gmart.gmart.domain.Delivery;
import gmart.gmart.repository.delivery.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 배송 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository; //배송 레파지토리

    /**
     * [저장]
     * @param delivery 배송 엔티티
     */
    @Transactional
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
