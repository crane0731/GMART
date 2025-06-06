package gmart.gmart.repository.store;

import gmart.gmart.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상점 레파지토리
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
}
