package gmart.gmart.repository.adminmessage;

import gmart.gmart.domain.AdminMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 관리자 메시지 레파지토리
 */
public interface AdminMessageRepository extends JpaRepository<AdminMessage, Long> {
}
