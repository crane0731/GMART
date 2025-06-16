package gmart.gmart.repository.gmoney;

import gmart.gmart.domain.log.GMoneyLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 건머니 거래 로그 레파지토리
 */
public interface GMoneyLogRepository extends JpaRepository<GMoneyLog, Long> {
}
