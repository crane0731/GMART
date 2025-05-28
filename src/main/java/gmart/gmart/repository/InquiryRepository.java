package gmart.gmart.repository;

import gmart.gmart.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 문의 레파지토리
 */
public interface InquiryRepository extends JpaRepository<Inquiry, Long>,InquiryRepositoryCustom {

}
