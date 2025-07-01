package gmart.gmart.service.gpoint;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gpoint.GPointChargeLogListResponseDto;
import gmart.gmart.dto.gpoint.SearchGPointChargeLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GPointCustomException;
import gmart.gmart.repository.gpoint.GPointChargeLogRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * 건포인트 충전 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPointChargeService {

    private final MemberService memberService; //회원 서비스
    private final GPointChargeLogRepository gPointChargeLogRepository; //건포인트 충전 로그 레파지토리


    /**
     * [서비스 로직]
     * 회원 자신의 건포인트 충전 로그 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return  List<GPointChargeLogListResponseDto> 응답 DTO 리스트
     */
    public PagedResponseDto<GPointChargeLogListResponseDto> findAllLogs(SearchGPointChargeLogCondDto condDto,int page){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //건포인트 충전 로그 조회 + 응답 DTO 리스트 생성 + 반환
        return getChargeLogs(member, condDto,page);

    }

    /**
     * [서비스 로직]
     * 검색 조건에 따라 건포인트 충전 로그 리스트를 조회 한 후 응답 DTO를 생성
     * @param member 회원 엔티티
     * @param condDto 검색 조건 DTO
     * @param page 페이지 번호
     * @return PagedResponseDto<GPointChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    public PagedResponseDto<GPointChargeLogListResponseDto> getChargeLogs(Member member, SearchGPointChargeLogCondDto condDto,int page){

        Page<GPointChargeLog> pageList = gPointChargeLogRepository.findAllByCond(member, condDto, createPageable(page));
        List<GPointChargeLogListResponseDto> content = pageList.getContent().stream().map(GPointChargeLogListResponseDto::create).toList();

        return createPagedResponseDto(content,pageList);

    }


    /**
     * [조회]
     * 아이디 PK 값으로 단일 조회
     * @param id 건포인트 충전 로그 아이디
     * @return GPointChargeLog 건포인트 충전 로그 엔티티
     */
    public GPointChargeLog findById(long id) {
        return gPointChargeLogRepository.findById(id).orElseThrow(()->new GPointCustomException(ErrorMessage.NOT_FOUND_GPOINT_CHARGE_LOG));
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



    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<GPointChargeLogListResponseDto> createPagedResponseDto(List<GPointChargeLogListResponseDto> content, Page<GPointChargeLog> page) {
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
