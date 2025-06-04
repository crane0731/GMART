package gmart.gmart.repository.gundam;

import gmart.gmart.domain.Gundam;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 커스텀 건담 정보 레파지토리 인터페이스
 */
public interface GundamRepositoryCustom {

    List<Gundam> findAllByCond(SearchGundamCondDto cond);

}
