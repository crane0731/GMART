package gmart.gmart.repository.gmoney;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 커스텀 건머니 충전로그 레파지토리
 */
public interface GMoneyChargeLogRepositoryCustom {

    Page<GMoneyChargeLog> findAllByCond(Member member, SearchGMoneyChargeLogCondDto cond, Pageable pageable);
}
