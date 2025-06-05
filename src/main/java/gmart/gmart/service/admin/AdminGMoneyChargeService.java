package gmart.gmart.service.admin;


import gmart.gmart.domain.Member;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.service.gmoney.GMoneyChargeService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 건머니 충전 관리 서비스
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminGMoneyChargeService {

    private final MemberService memberService; //회원 서비스
    private final GMoneyChargeService gMoneyChargeService; //건머니 충전 서비스



    /**
     * [서비스 로직]
     * 관리자 - 건머니 충전 로그 목록 조회(리스트)
     * @param MemberId 회원 아이디
     * @param condDto 검색 조건 DTO
     * @return List<GMoneyChargeLogListResponseDto> 응답 DTO 리스트
     */
    public List<GMoneyChargeLogListResponseDto> findAllLogs(Long MemberId, SearchGMoneyChargeLogCondDto condDto) {

        //회원 조회
        Member member = memberService.findById(MemberId);

        //건머니 충전 로그 조회 + DTO 변환 + 반환
        return gMoneyChargeService.getChargeLogs(member, condDto);

    }

}
