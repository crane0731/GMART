package gmart.gmart.repository.inquiry;

import gmart.gmart.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 문의 레파지토리
 */
public interface InquiryRepository extends JpaRepository<Inquiry, Long>,InquiryRepositoryCustom {

}
