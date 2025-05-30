package gmart.gmart.service;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.MemberSuspension;
import gmart.gmart.repository.MemberSuspensionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 회원 계정 일시 정지 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberSuspensionService {

    private final MemberSuspensionRepository memberSuspensionRepository;

    /**
     * 저장
     * @param memberSuspension
     */
    public void save(MemberSuspension memberSuspension) {
        memberSuspensionRepository.save(memberSuspension);
    }

    /**
     * 삭제
     * @param memberSuspension
     */
    @Transactional
    public void delete(MemberSuspension memberSuspension) {
        memberSuspensionRepository.delete(memberSuspension);
    }

    /**
     * 이미 계정 정지중인 회원인지 확인하는 로직
     * @param member
     * @return
     */
    public boolean isCurrentlySuspended(Member member) {
        return memberSuspensionRepository.existsActiveSuspension(member.getId(), LocalDateTime.now());
    }


    /**
     * 일정 시간마다 계정 정지 중인 회원을 조회하여,
     * 정지 만료일이 지난 경우 계정 정지를 해제하는 로직.
     * 매일 새벽 3시에 실행됨. (초 분 시 일 월 요일)
     */
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void releaseMemberSuspension(){

        //현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 정지 만료일이 지난 회원 정지 기록 삭제
        try {
            log.info("[회원 정지 해제] 만료된 계정 정지를 해제합니다. 실행 시각: {}", now);
            memberSuspensionRepository.findExpired(now).forEach(MemberSuspension::releaseMemberActiveStatus);
        } catch (Exception e) {
            log.error("[회원 정지 해제 실패] 에러 발생: {}", e.getMessage(), e);
        }


    }


}
