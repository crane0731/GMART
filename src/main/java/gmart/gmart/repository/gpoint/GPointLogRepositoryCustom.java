package gmart.gmart.repository.gpoint;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GPointLog;
import gmart.gmart.dto.gpoint.SearchGPointLogCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 건포인트 거래 로그 레파지토리
 */
public interface GPointLogRepositoryCustom {

    Page<GPointLog> findAllByCond(Member member, SearchGPointLogCondDto cond, Pageable pageable);

}
