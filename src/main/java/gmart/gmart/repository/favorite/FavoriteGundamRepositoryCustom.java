package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;

import java.util.List;

/**
 * 관심 건담 커스텀 레파지토리 구현체 클래스
 */
public interface FavoriteGundamRepositoryCustom {

    List<FavoriteGundam> findAllByMemberAndCond(SearchFavoriteGundamCondDto cond, Member member);

}
