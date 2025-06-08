package gmart.gmart.repository.store;

import gmart.gmart.domain.Store;
import gmart.gmart.dto.store.SearchStoreCondDto;

import java.util.List;

/**
 * 커스텀 상점 레파지토리
 */
public interface StoreRepositoryCustom {

    List<Store> findAllByCond(SearchStoreCondDto cond);

}
