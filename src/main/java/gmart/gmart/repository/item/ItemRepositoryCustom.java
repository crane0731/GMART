package gmart.gmart.repository.item;

import gmart.gmart.domain.Item;
import gmart.gmart.dto.item.SearchItemCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 상품 레파지토리 커스텀
 */
public interface ItemRepositoryCustom {

    Page<Item> findAllByCond(SearchItemCondDto cond, Pageable pageable);


}
