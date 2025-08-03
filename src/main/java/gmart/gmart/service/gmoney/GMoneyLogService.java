package gmart.gmart.service.gmoney;

import gmart.gmart.command.CreateGMoneyLogCommand;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.GMoneyLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.repository.gmoney.GMoneyLogRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 건머니 거래로그 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GMoneyLogService {

    private final MemberService memberService;//회원 서비스

    private final GMoneyLogRepository gMoneyLogRepository;//건머니 거래로그 레파지토리



    /**
     * [서비스 로직]
     * 조건에 따라 건머니 거래 로그를 조회
     * @param condDto 검색 조건 DTO
     * @param page 페이지 번호
     * @return PagedResponseDto<GMoneyLogListResponseDto>
     */
    public PagedResponseDto<GMoneyLogListResponseDto> findAllLogs(SearchGMoneyLogCondDto condDto, int page){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //현재 로그인한 회원의 건머니 충전 로그 조회 + 응답 DTO 리스트 생성
        return getLogs(member,condDto,page);
    }



    /**
     * [서비스 로직]
     * 건머니 거래 로그 생성
     * @param member 회원
     * @param order 주문
     * @param gMoneyDeltaType 건머니 변화 타입
     * @param description 설명
     * @param deltaGMoney 건머니 변화량
     * @param beforeGMoney 이전 건머니
     * @param afterGMoney 이후 건머니
     */
    @Transactional
    public void createLog(Member member, Order order, GMoneyDeltaType gMoneyDeltaType,
                          String description, Long deltaGMoney, Long beforeGMoney, Long afterGMoney) {

        //커맨드 생성
        CreateGMoneyLogCommand command = createCommand(member, order, gMoneyDeltaType, description, deltaGMoney, beforeGMoney, afterGMoney);

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
    private CreateGMoneyLogCommand createCommand(Member member, Order order, GMoneyDeltaType gMoneyDeltaType, String description, Long deltaGMoney, Long beforeGMoney, Long afterGMoney) {
        return CreateGMoneyLogCommand.builder()
                .member(member)
                .order(order)
                .gMoneydeltaType(gMoneyDeltaType)
                .description(description)
                .deltaGMoney(deltaGMoney)
                .beforeGMoney(beforeGMoney)
                .afterGMoney(afterGMoney)
                .build();
    }


    //==거래로그를 조회하고 DTO 로 변환==//
    private PagedResponseDto<GMoneyLogListResponseDto> getLogs(Member member, SearchGMoneyLogCondDto condDto, int page) {
        Page<GMoneyLog> pageList = gMoneyLogRepository.findAllByCond(member, condDto, createPageable(page));
        List<GMoneyLogListResponseDto> content = pageList.getContent().stream().map(GMoneyLogListResponseDto::create).toList();
        return createPagedResponseDto(content,pageList);


    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<GMoneyLogListResponseDto> createPagedResponseDto(List<GMoneyLogListResponseDto> content, Page<GMoneyLog> page) {
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

    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(page, 10);
    }

}
