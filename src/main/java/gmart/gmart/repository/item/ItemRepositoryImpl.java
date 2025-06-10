package gmart.gmart.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Item;
import gmart.gmart.domain.QGundam;
import gmart.gmart.domain.QItem;
import gmart.gmart.domain.QItemGundam;
import gmart.gmart.dto.item.SearchItemCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 상품 레파지토리 커스텀 구현체 클래스
 */
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * 검색 조건에 따라 상품 리스트 조회
     * @param cond 검색 조건
     * @param pageable 페이징 처리를 위한 객체
     * @return  Page<Item> 상품 엔티티 페이지
     */
    @Override
    public Page<Item> findAllByCond(SearchItemCondDto cond, Pageable pageable) {

        QItem item = QItem.item;
        QItemGundam itemGundam = QItemGundam.itemGundam;
        QGundam gundam = QGundam.gundam;
        BooleanBuilder builder = new BooleanBuilder();

        if(cond.getTitle() != null&&!cond.getTitle().isBlank()) {
            builder.and(item.title.containsIgnoreCase(cond.getTitle()));
        }

        if(cond.getGundamId()!=null && cond.getGundamId()!=0) {
            builder.and(gundam.id.eq(cond.getGundamId()));
        }

        if(cond.getGundamGrade()!=null){
            builder.and(item.grade.eq(cond.getGundamGrade()));
        }

        if(cond.getBoxStatus()!=null){
            builder.and(item.boxStatus.eq(cond.getBoxStatus()));
        }

        if(cond.getPaintStatus()!=null){
            builder.and(item.paintStatus.eq(cond.getPaintStatus()));
        }

        if(cond.getDealType()!=null){
            builder.and(item.dealType.eq(cond.getDealType()));
        }

        if (cond.getSaleStatus()!=null){
            builder.and(item.saleStatus.eq(cond.getSaleStatus()));
        }

        //정렬 타입 설정
        OrderSpecifier<?> sortOrder = null;

        if (cond.getSortType() != null) {
            switch (cond.getSortType()) {
                case PRICE_HIGH -> sortOrder = item.itemPrice.desc();
                case PRICE_LOW -> sortOrder = item.itemPrice.asc();
                case VIEW_COUNT -> sortOrder = item.viewCount.desc();
                case NEWEST -> sortOrder = item.createdDate.desc();
            }
        }else{
            sortOrder = item.createdDate.desc();
        }


        List<Item> content = query
                .select(item)
                .from(item)
                .leftJoin(item.itemGundams, itemGundam)
                .leftJoin(itemGundam.gundam, gundam)
                .where(builder)
                .orderBy(sortOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(item.count())
                .from(item)
                .leftJoin(item.itemGundams, itemGundam)
                .leftJoin(itemGundam.gundam, gundam)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
