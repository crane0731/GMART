package gmart.gmart.service.gpoint;

import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.repository.gpoint.GPointChargeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * 건포인트 충전 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPointChargeService {


    private final GPointChargeLogRepository gPointChargeLogRepository; //건포인트 충전 로그 레파지토리




    /**
     * [조회]
     * 아이디 PK 값으로 단일 조회
     * @param id 건포인트 충전 로그 아이디
     * @return GPointChargeLog 건포인트 충전 로그 엔티티
     */
    public GPointChargeLog findById(long id) {
        return gPointChargeLogRepository.findById(id).orElse(null);
    }

    /**
     * [저장]
     * @param gPointChargeLog 건포인트 충전 로그 엔티티
     */
    @Transactional
    public void save(GPointChargeLog gPointChargeLog) {
        gPointChargeLogRepository.save(gPointChargeLog);
    }

    /**
     * [삭제]
     * @param gPointChargeLog 건포인트 충전 로그 엔티티
     */
    @Transactional
    public void delete(GPointChargeLog gPointChargeLog) {
        gPointChargeLogRepository.delete(gPointChargeLog);
    }

}
