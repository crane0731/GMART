package gmart.gmart.repository.gundam;

import gmart.gmart.domain.Gundam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 건담 정보 레파지토리
 */

public interface GundamRepository  extends JpaRepository<Gundam, Long> ,GundamRepositoryCustom {

}
