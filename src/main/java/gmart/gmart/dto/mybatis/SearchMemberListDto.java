package gmart.gmart.dto.mybatis;

import gmart.gmart.dto.enums.MemberSortType;
import gmart.gmart.dto.enums.SortDirection;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원리스트를 조건에 따라 필터링해서 조회하기 위한 DTO
 * - 닉네임으로 검색
 * - 로그인아이디(이메일)로 검색
 * - 정렬 타입:  제제 횟수 , 신고당한 횟수, 매너 점수, 총 결제량
 * - 정렬 방향 : 오름차순, 내림차순
 */
@Getter
@Setter
public class SearchMemberListDto {

    private String nickname;
    private String loginId;
    private MemberSortType sortType; //suspension_count,reported_count,manner_point,total_spent
    private SortDirection sortDirection; //ASC, DESC

}
