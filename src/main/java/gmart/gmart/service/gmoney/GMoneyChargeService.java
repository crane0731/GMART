package gmart.gmart.service.gmoney;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.GMoneyChargeRequestDto;
import gmart.gmart.dto.gmoney.GMoneyRefundRequestDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.repository.gmoney.GMoneyChargeLogRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 건머니 충전 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GMoneyChargeService {

    private final MemberService memberService; //회원 서비스

    private final GMoneyChargeLogRepository gMoneyChargeLogRepository; //건머니 충전 로그 레파지토리

    /**
     * [서비스 로직]
     * 건머니 충전 + 건머니 충전 로그 생성
     * @param requestDto 건머니 충전 요청 DTO
     */
    @Transactional
    public void chargeGMoney(GMoneyChargeRequestDto requestDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //충전 전 금액
        Long beforeChargeMoney = member.getGMoney();

        //충전할 금액
        Long chargeMoney = requestDto.getPrice();

        //건머니 충전
        charge(member, chargeMoney);

        //로그 저장
        saveLog(member, beforeChargeMoney, chargeMoney,ChargeType.PAYMENT);

    }

    /**
     * [서비스 로직]
     * 건머니 환불 + 건머니 환불 로그 생성
     * @param requestDto 건머니 환불 요청 DTO
     */
    @Transactional
    public void refundGMoney(GMoneyRefundRequestDto requestDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //환불 전 금액
        Long beforeRefundMoney = member.getGMoney();

        //환불 할 금액
        Long refundMoney= requestDto.getPrice();

        //건머니 환불
        refund(member,refundMoney);

        //로그 저장
        saveLog(member, beforeRefundMoney, refundMoney,ChargeType.REFUND);
    }

    /**
     * [서비스 로직]
     * 조건에 따라 건머니 충전로그 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<GMoneyChargeLogListResponseDto> 응답 DTO 리스트
     */
    public List<GMoneyChargeLogListResponseDto> findAllLogs(SearchGMoneyChargeLogCondDto condDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //현재 로그인한 회원의 건머니 충전 로그 조회 + 응답 DTO 리스트 생성
        return getChargeLogs(member,condDto);
    }

    /**
     * [서비스 로직]
     * 조건에 따라 건머니 충전로그를 조회하고 DTO 로 변환
     * @param member 회원 엔티티
     * @param condDto 검색 조건 DTO
     * @return List<GMoneyChargeLogListResponseDto> 응답 DTO 리스트
     */
    public List<GMoneyChargeLogListResponseDto> getChargeLogs(Member member,SearchGMoneyChargeLogCondDto condDto) {
        return gMoneyChargeLogRepository.findAllByCond(member,condDto)
                .stream()
                .map(GMoneyChargeLogListResponseDto::create)
                .toList();
    }


    //==건 머니 충전 메서드==//
    private void charge(Member member, Long chargeMoney) {
        member.chargeGMoney(chargeMoney);
    }

    //==건 머니 충전 메서드==//
    private void refund(Member member, Long chargeMoney) {
        member.refundGMoney(chargeMoney);
    }


    //==로그 저장 메서드==//
    private void saveLog(Member member, Long beforeChargeMoney, Long chargeMoney,ChargeType chargeType) {
        //건머니 충전 로그 객체 생성
        GMoneyChargeLog gMoneyChargeLog = GMoneyChargeLog.create(member, beforeChargeMoney, chargeMoney, chargeType);

        //로그 저장
        gMoneyChargeLogRepository.save(gMoneyChargeLog);
    }


    //==회원 아이디를 통해 건머니 충전 로그리스트를 조회하고 응답 DTO 리스트를 생성하는 메서드==//

}
