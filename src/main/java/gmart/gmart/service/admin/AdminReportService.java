package gmart.gmart.service.admin;

import gmart.gmart.domain.Report;
import gmart.gmart.dto.enums.AdminReportAcceptStatus;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.report.AdminAcceptReportRequestDto;
import gmart.gmart.dto.report.ReportDetailsResponseDto;
import gmart.gmart.dto.report.ReportListResponseDto;
import gmart.gmart.dto.report.SearchReportCondDto;
import gmart.gmart.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 - 신고 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final ReportService reportService; //신고 서비스


    /**
     * [서비스 로직]
     * 관리자가 신고를 처리
     * 수락 / 거절
     * @param reportId 신고 아이디
     * @param requestDto 요청 DTO
     */
    @Transactional
    public void changeReportStatus(Long reportId,AdminAcceptReportRequestDto requestDto){

        //수락 처리할 신고
        Report report = reportService.findById(reportId);

        if(requestDto.getStatus()==AdminReportAcceptStatus.ACCEPT){
            report.accept();
        }else {
            report.reject();
        }
    }

    /**
     * [서비스 로직]
     * 관리자 신고 내용 상세 조회
     * @param reportId 신고 아이디
     * @return ReportDetailsResponseDto 응답 DTO
     */
    public ReportDetailsResponseDto getReportDetails(Long reportId){

        //신고 엔티티 조회
        Report report = reportService.findById(reportId);

        // 응답 DTO 생성 + 반환
        return ReportDetailsResponseDto.create(report);

    }

    /**
     * [서비스 로직]
     * 관리자가 검색조건을 통해 신고 목록을 조회 + 페이징
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<ReportListResponseDto> 페이징 응답 DTO
     */
    public PagedResponseDto<ReportListResponseDto> getAllReports(SearchReportCondDto condDto,int page){

        Page<Report> pageList = reportService.findAllByCond(condDto, createPageable(page));

        List<ReportListResponseDto> content = pageList.getContent()
                .stream()
                .map(ReportListResponseDto::create)
                .toList();

        return createPagedResponseDto(content,pageList);

    }



    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(page, 10);
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<ReportListResponseDto> createPagedResponseDto(List<ReportListResponseDto> content, Page<Report> page) {
        return PagedResponseDto.<ReportListResponseDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();

    }


}
