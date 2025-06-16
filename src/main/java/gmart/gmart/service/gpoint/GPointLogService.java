package gmart.gmart.service.gpoint;


import gmart.gmart.command.CreateGPointLogCommand;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.enums.GPointDeltaType;
import gmart.gmart.domain.log.GPointLog;
import gmart.gmart.repository.gpoint.GPointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 건포인트 거래로그 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPointLogService {

    private final GPointLogRepository gPointLogRepository; //건포인트 거래 로그 레파지토리

    /**
     * [서비스 로직]
     * 건포인트 거래 로그 등록
     * @param memberId 회원 아이디
     * @param orderId 주문 아이디
     * @param gPointDeltaType 건포인트 변화 타입
     * @param description 설명
     * @param deltaGPoint 건포인트 변화량
     * @param beforeGPoint 이전 건포인트
     * @param afterGPoint 이후 건포인트
     */
    @Transactional
    public void createLog(Long memberId, Long orderId, GPointDeltaType gPointDeltaType,
                          String description, Long deltaGPoint, Long beforeGPoint, Long afterGPoint){

        //커맨드 생성
        CreateGPointLogCommand command = createCommand(memberId, orderId, gPointDeltaType, description, deltaGPoint, beforeGPoint, afterGPoint);

        //건포인트 거래 로그 생성
        GPointLog gPointLog = GPointLog.create(command);

        //로그 저장
        save(gPointLog);
    }


    /**
     * [저장]
     * @param gPointLog
     */
    @Transactional
    public void save(GPointLog gPointLog) {
        gPointLogRepository.save(gPointLog);
    }

    //==커맨드 생성==//
    private CreateGPointLogCommand createCommand(Long memberId, Long orderId, GPointDeltaType gPointDeltaType, String description, Long deltaGPoint, Long beforeGPoint, Long afterGPoint) {
        return CreateGPointLogCommand.builder()
                .memberId(memberId)
                .orderId(orderId)
                .gPointDeltaType(gPointDeltaType)
                .description(description)
                .deltaGPoint(deltaGPoint)
                .beforeGPoint(beforeGPoint)
                .afterGPoint(afterGPoint)
                .build();
    }
}
