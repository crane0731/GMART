package gmart.gmart.repository.mybatis;


import gmart.gmart.domain.Member;
import gmart.gmart.dto.mybatis.MemberListResponseDto;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Mybatis - 회원 레파지토리
 */
@Repository
@RequiredArgsConstructor
public class MybatisMemberRepository {

    private final MemberMapper memberMapper;

    /**
     * 모든 회원을 조건에따라 조회하는 동적쿼리 메서드
     * @param searchMemberListDto 회원검색DTO
     * @return 회원 리스트
     */
    public List<MemberListResponseDto> findAll(SearchMemberListDto searchMemberListDto){
        return memberMapper.findAll(searchMemberListDto);
    }

}
