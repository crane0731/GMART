package gmart.gmart.repository.adminmessage;

import gmart.gmart.domain.AdminMessage;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 관리자 메시지 레파지토리
 */
public interface AdminMessageRepositoryCustom {

    /**
     * 검잭 조건 에따라 특정 회원의 관리자 메시지 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<AdminMessage> 페이징된 관리자 엔티티 목록
     */
    Page<AdminMessage> findAllByCond(Member member, SearchAdminMessageCondDto cond, Pageable pageable);

}
