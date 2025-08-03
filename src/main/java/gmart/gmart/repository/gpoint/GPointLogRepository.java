package gmart.gmart.repository.gpoint;

import gmart.gmart.domain.log.GPointLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 건포인트 거래 로그 레파지토리
 */
public interface GPointLogRepository extends JpaRepository<GPointLog, Long> ,GPointLogRepositoryCustom {

}
