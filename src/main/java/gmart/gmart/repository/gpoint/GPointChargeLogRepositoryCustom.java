package gmart.gmart.repository.gpoint;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.dto.gpoint.SearchGPointChargeLogCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 커스텀 건포인트 충전로그 레파지토리
 */
public interface GPointChargeLogRepositoryCustom {

    Page<GPointChargeLog> findAllByCond(Member member, SearchGPointChargeLogCondDto cond, Pageable pageable);

}
