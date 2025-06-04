package gmart.gmart.repository.gmoney;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.log.GMoneyChargeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 건머니 충전 로그 레파지토리
 */
public interface GMoneyChargeLogRepository extends JpaRepository<GMoneyChargeLog, Long> ,GMoneyChargeLogRepositoryCustom{

}
