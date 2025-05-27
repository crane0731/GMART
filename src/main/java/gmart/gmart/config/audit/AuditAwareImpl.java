package gmart.gmart.config.audit;

import gmart.gmart.domain.userdetail.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 누가 이 데이터를 생성하거나 수정했는지 기록해주는 인터페이스를 구현한 클래스.
 */
@Slf4j
public class AuditAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        log.info("getCurrentAuditor() 호출됨!");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //인증정보가 없거나 유효하지 않다면 0을 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("너가 문제야");
            return Optional.of(0L);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetail) {
            log.info("아닌데??");
            return Optional.of(userDetail.getId());
        }

        log.warn("예상치 못한 principal: {}", principal);
        return Optional.empty(); // 익명 회원이거나 예상치 못한 principal 타입일 경우
    }
}

