package gmart.gmart.repository.item;

import gmart.gmart.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 레파지토리
 */
public interface ItemRepository extends JpaRepository<Item, Long>,ItemRepositoryCustom {
}
