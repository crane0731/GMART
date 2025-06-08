package gmart.gmart.repository.store;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.QStore;
import gmart.gmart.domain.Store;
import gmart.gmart.dto.enums.StoreSortType;
import gmart.gmart.dto.store.SearchStoreCondDto;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * 커스텀 상점 레파지토리 구현체 클래스
 */
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public StoreRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 검색 조건에 따라 상점 리스트를 조회하는 동적 쿼리 메서드
     * @param cond 검색 조건 DTO
     * @return List<Store> 상점 엔티티 리스트
     */
    @Override
    public List<Store> findAllByCond(SearchStoreCondDto cond) {

        QStore store = QStore.store;
        BooleanBuilder builder = new BooleanBuilder();

        if(cond.getName()!=null && !cond.getName().isBlank()){
            builder.and(store.name.containsIgnoreCase(cond.getName()));
        }

        //정렬 타입 설정
        OrderSpecifier<?> sortOrder = null;

        if(cond.getSortType()==null || cond.getSortType().equals(StoreSortType.HIGH_RATING)){
            sortOrder=store.rating.desc();
        }
        else if(cond.getSortType().equals(StoreSortType.HIGH_REVIEW)){
            sortOrder=store.rating.desc();
        }

        return query
                .select(store)
                .from(store)
                .where(builder)
                .orderBy(sortOrder)
                .limit(10)
                .fetch();

    }
}
