package gmart.gmart.repository.member;

import gmart.gmart.domain.Member;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 회원 레파지토리
 */
public interface MemberRepositoryCustom {

    Page<Member> findAllByCond(SearchMemberListDto cond, Pageable pageable);

}
