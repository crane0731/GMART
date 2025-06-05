package gmart.gmart.repository.gpoint;

import gmart.gmart.domain.log.GPointChargeLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 건포인트 충전 로그 레파지토리
 */
public interface GPointChargeLogRepository  extends JpaRepository<GPointChargeLog, Long>,GPointChargeLogRepositoryCustom {
}
