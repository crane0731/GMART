package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 관심 건담 커스텀 레파지토리 구현체 클래스
 */
public interface FavoriteGundamRepositoryCustom {

    Page<FavoriteGundam> findAllByMemberAndCond(SearchFavoriteGundamCondDto cond, Member member, Pageable pageable);

}
