package gmart.gmart.repository.gmoney;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;

import java.util.List;

/**
 * 커스텀 건머니 충전로그 레파지토리
 */
public interface GMoneyChargeLogRepositoryCustom {

    List<GMoneyChargeLog> findAllByCond(Member member,SearchGMoneyChargeLogCondDto cond);
}
