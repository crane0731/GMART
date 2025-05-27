package gmart.gmart.repository.mybatis;

import gmart.gmart.domain.Member;
import gmart.gmart.dto.mybatis.MemberListResponseDto;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis Mapper - Member
 */
@Mapper
public interface MemberMapper {

    /**
     * 모든 회원을 조회하는 동적 쿼리 메서드
     * @param searchMemberListDto
     * @return 회원 리스트
     */
    List<MemberListResponseDto> findAll(@Param("searchMemberListDto") SearchMemberListDto searchMemberListDto);
}
