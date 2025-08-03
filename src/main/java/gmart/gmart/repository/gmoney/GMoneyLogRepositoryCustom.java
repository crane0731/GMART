package gmart.gmart.repository.gmoney;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 건머니 거래 로그 레파지토리
 */
public interface GMoneyLogRepositoryCustom {

    Page<GMoneyLog> findAllByCond(Member member, SearchGMoneyLogCondDto cond, Pageable pageable);

}
