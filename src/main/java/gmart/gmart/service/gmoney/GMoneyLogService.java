package gmart.gmart.service.gmoney;

import gmart.gmart.command.CreateGMoneyLogCommand;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.repository.gmoney.GMoneyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 건머니 거래로그 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GMoneyLogService {

    private final GMoneyLogRepository gMoneyLogRepository;//건머니 거래로그 레파지토리

    /**
     * [서비스 로직]
     * 건머니 거래 로그 생성
     * @param memberId 회원 아이디
     * @param orderId 주문 아이디
     * @param gMoneyDeltaType 건머니 변화 타입
     * @param description 설명
     * @param deltaGMoney 건머니 변화량
     * @param beforeGMoney 이전 건머니
     * @param afterGMoney 이후 건머니
     */
    @Transactional
    public void createLog(Long memberId, Long orderId, GMoneyDeltaType gMoneyDeltaType,
                          String description, Long deltaGMoney, Long beforeGMoney, Long afterGMoney) {

        //커맨드 생성
        CreateGMoneyLogCommand command = createCommand(memberId, orderId, gMoneyDeltaType, description, deltaGMoney, beforeGMoney, afterGMoney);

        //건머니 로그 생성
        GMoneyLog gMoneyLog = GMoneyLog.create(command);

        //건머니 로그 저장
        save(gMoneyLog);

    }

    /**
     * [저장]
     * @param gMoneyLog 건머니 거래 로그 엔티티
     */
    @Transactional
    public void save(GMoneyLog gMoneyLog) {
        gMoneyLogRepository.save(gMoneyLog);
    }

    //==커맨드 생성==//
    private CreateGMoneyLogCommand createCommand(Long memberId, Long orderId, GMoneyDeltaType gMoneyDeltaType, String description, Long deltaGMoney, Long beforeGMoney, Long afterGMoney) {
        return CreateGMoneyLogCommand.builder()
                .memberId(memberId)
                .orderId(orderId)
                .gMoneydeltaType(gMoneyDeltaType)
                .description(description)
                .deltaGMoney(deltaGMoney)
                .beforeGMoney(beforeGMoney)
                .afterGMoney(afterGMoney)
                .build();
    }

}
