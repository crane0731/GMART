package gmart.gmart.service.admin;

import gmart.gmart.repository.adminmessage.AdminMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 메시지 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMessageService {

    private final AdminMessageRepository adminMessageRepository; //관리자 메시지 레파지토리

    /**
     * 관리자 - 메시지 등록
     */

    /**
     * 관리자 - 메시지 삭제
     */

    /**
     * 관리자 - 특정 회원의 메시지 리스트 조회
     */

    /**
     * 회원이 자신의 메시지 삭제
     */

    /**
     * 회원이 자신의 메시지 리스트 조회
     */



}
