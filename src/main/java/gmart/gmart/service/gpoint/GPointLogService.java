package gmart.gmart.service.gpoint;


import gmart.gmart.command.CreateGPointLogCommand;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.enums.GPointDeltaType;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.domain.log.GPointLog;
import gmart.gmart.dto.gmoney.GMoneyLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import gmart.gmart.dto.gpoint.GPointLogListResponseDto;
import gmart.gmart.dto.gpoint.SearchGPointLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.repository.gpoint.GPointLogRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 건포인트 거래로그 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPointLogService {

    private final MemberService memberService; //회원 서비스
    private final GPointLogRepository gPointLogRepository; //건포인트 거래 로그 레파지토리


    /**
     * [서비스 로직]
     * 조건에 따라 건포인트 거래 로그를 조회
     * @param condDto 검색 조건 DTO
     * @param page 페이지 번호
     * @return PagedResponseDto<GPointLogListResponseDto>
     */
    public PagedResponseDto<GPointLogListResponseDto> findAllLogs(SearchGPointLogCondDto condDto, int page){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //현재 로그인한 회원의 건머니 충전 로그 조회 + 응답 DTO 리스트 생성
        return getLogs(member,condDto,page);
    }



    /**
     * [서비스 로직]
     * 건포인트 거래 로그 등록
     * @param member 회원
     * @param order 주문
     * @param gPointDeltaType 건포인트 변화 타입
     * @param description 설명
     * @param deltaGPoint 건포인트 변화량
     * @param beforeGPoint 이전 건포인트
     * @param afterGPoint 이후 건포인트
     */
    @Transactional
    public void createLog(Member member, Order order, GPointDeltaType gPointDeltaType,
                          String description, Long deltaGPoint, Long beforeGPoint, Long afterGPoint){

        //커맨드 생성
        CreateGPointLogCommand command = createCommand(member, order, gPointDeltaType, description, deltaGPoint, beforeGPoint, afterGPoint);

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
    private CreateGPointLogCommand createCommand(Member member, Order order, GPointDeltaType gPointDeltaType, String description, Long deltaGPoint, Long beforeGPoint, Long afterGPoint) {
        return CreateGPointLogCommand.builder()
                .member(member)
                .order(order)
                .gPointDeltaType(gPointDeltaType)
                .description(description)
                .deltaGPoint(deltaGPoint)
                .beforeGPoint(beforeGPoint)
                .afterGPoint(afterGPoint)
                .build();
    }



    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(page, 10);
    }

    //==거래로그를 조회하고 DTO 로 변환==//
    private PagedResponseDto<GPointLogListResponseDto> getLogs(Member member, SearchGPointLogCondDto condDto, int page) {
        Page<GPointLog> pageList = gPointLogRepository.findAllByCond(member, condDto, createPageable(page));
        List<GPointLogListResponseDto> content = pageList.getContent().stream().map(GPointLogListResponseDto::create).toList();
        return createPagedResponseDto(content,pageList);


    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<GPointLogListResponseDto> createPagedResponseDto(List<GPointLogListResponseDto> content, Page<GPointLog> page) {
        return new PagedResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }
}
