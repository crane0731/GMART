package gmart.gmart.service.admin;

import gmart.gmart.domain.Member;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.repository.MemberRepository;
import gmart.gmart.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 관리자 회원 관리 서비스
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberService {

    private final MemberService memberService;

    /**
     * 회원 상세 조회
     * @param memberId
     * @return
     */
    public MemberInfoResponseDto findMemberDetailInfo(Long memberId){

        //회원 아이디로 회원 조회
        Member member = memberService.findById(memberId);

        //DTO 반환
        return MemberInfoResponseDto.createDto(member);

    }

}
