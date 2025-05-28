package gmart.gmart.repository;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 커스텀 문의 레파지토리 인터페이스
 */
public interface InquiryRepositoryCustom {

    Page<Inquiry> findAllByCond(SearchInquiryCondDto cond, Pageable pageable);

}
