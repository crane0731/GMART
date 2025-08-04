package gmart.gmart.repository.item;

import gmart.gmart.domain.*;
import gmart.gmart.dto.item.SearchItemCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 상품 레파지토리 커스텀
 */
public interface ItemRepositoryCustom {

    Page<Item> findAllByCond(SearchItemCondDto cond, Pageable pageable);
    Page<Item> findAllByCondAndMember(Member member, SearchItemCondDto cond, Pageable pageable);
    Page<Item> findAllByCondAndStore(Store store, SearchItemCondDto cond, Pageable pageable);
    Page<Item> findAllByGundams(List<Gundam> gundams, Pageable pageable);

}
