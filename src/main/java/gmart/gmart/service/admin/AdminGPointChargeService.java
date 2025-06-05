package gmart.gmart.service.admin;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.dto.gpoint.GPointChargeRequestDto;
import gmart.gmart.dto.gpoint.GPointRefundRequestDto;
import gmart.gmart.service.gpoint.GPointChargeService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 건포인트 충전 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminGPointChargeService {

    private final MemberService memberService; //회원 서비스
    private final GPointChargeService gPointChargeService;//건포인트 충전 서비스


    /**
     * [서비스 로직]
     * 모든 회원 건포인트 충전 + 충전 로그 저장
     * @param requestDto 충전 요청 DTO
     */
    @Transactional
    public void allMembersChargeGPoint(GPointChargeRequestDto requestDto){

        //모든 회원 조회
        List<Member> allMembers = memberService.findAll();

        //충전 할 포인트
        Long chargePoint = requestDto.getPoint();

        for (Member member : allMembers) {

            //충전 전 건포인트
            Long beforeChargePoint = member.getGPoint();

            //건포인트 충전 + 충전 후 건포인트 반환
            Long afterChargePoint = charge(member, chargePoint);

            //건포인트 충전 로그 생성 + DB 저장
            saveLog(member, ChargeType.valueOf(requestDto.getChargeType()),chargePoint, beforeChargePoint, afterChargePoint);
        }
    }

    /**
     * [서비스 로직]
     * 모든 회원 건포인트 회수 + 충전(회수) 로그 저장
     * @param requestDto 회수 요청 DTO
     */
    @Transactional
    public void allMembersRefundGPoint(GPointRefundRequestDto requestDto){

        //모든 회원 조회
        List<Member> allMembers = memberService.findAll();

        //회수 할 포인트
        Long refundPoint = requestDto.getPoint();

        for (Member member : allMembers) {

            //회수 전 포인트
            Long beforeRefundPoint = member.getGPoint();

            //건포인트 회수 + 회수 후 건포인트 반환
            Long afterRefundPoint = refund(member, refundPoint);

            //건포인트 충전(회수) 로그 생성 + DB 저장
            saveLog(member,ChargeType.REFUND,refundPoint, beforeRefundPoint, afterRefundPoint);
        }
    }

    /**
     * [서비스 로직]
     * 관리자 특정 회원 건포인트 충전
     * @param memberId 회원 아이디
     * @param requestDto 충전 요청 DTO
     */
    @Transactional
    public void chargeGPoint(Long memberId, GPointChargeRequestDto requestDto){

        //회원 조회
        Member member = memberService.findById(memberId);

        //충전 전 포인트
        Long beforeChargePoint = member.getGPoint();

        //충전 할 포인트
        Long chargePoint = requestDto.getPoint();

        //건포인트 충전 + 충전 후 건포인트 반환
        Long afterChargePoint = charge(member, chargePoint);

        //건포인트 충전 로그 생성 + DB 저장
        saveLog(member, ChargeType.valueOf(requestDto.getChargeType()),chargePoint, beforeChargePoint, afterChargePoint);
    }

    /**
     * [서비스 로직]
     * 관리자 특정 회원 건포인트 회수
     * @param memberId 회원 아이디
     * @param requestDto 회수 요청 DTO
     */
    @Transactional
    public void refundGPoint(Long memberId, GPointRefundRequestDto requestDto){
        //회원 조회
        Member member = memberService.findById(memberId);

        //회수 전 포인트
        Long beforeRefundPoint = member.getGPoint();

        //회수 할 포인트
        Long refundPoint = requestDto.getPoint();

        //건포인트 회수 + 회수 후 건포인트 반환
        Long afterRefundPoint = refund(member, refundPoint);

        //건포인트 충전(회수) 로그 생성 + DB 저장
        saveLog(member,ChargeType.REFUND,refundPoint, beforeRefundPoint, afterRefundPoint);
    }


    /**
     * 관리자 특정 회원 건포인트 로그 조회
     */


    //==건포인트 충전 + 충전 후 건포인트 반환 메서드==//
    private Long charge(Member member, Long chargePoint) {

        return member.chargeGPoint(chargePoint);
    }

    //==건포인트 회수 + 회수 후 건포인트 반환 메서드==//
    private Long refund(Member member, Long refundPoint) {
        return member.refundGPoint(refundPoint);
    }

    //==건포인트 충전 로그 생성 + DB 저장 메서드==//
    private void saveLog(Member member, ChargeType chargeType,Long chargePoint, Long beforeChargePoint, Long afterChargePoint) {
        GPointChargeLog gPointChargeLog = GPointChargeLog.create(member,chargeType , chargePoint, beforeChargePoint, afterChargePoint);
        gPointChargeService.save(gPointChargeLog);
    }



}
