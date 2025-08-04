package gmart.gmart.controller.admin;

import gmart.gmart.domain.enums.ReportStatus;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.report.AdminAcceptReportRequestDto;
import gmart.gmart.dto.report.ReportListResponseDto;
import gmart.gmart.dto.report.SearchReportCondDto;
import gmart.gmart.service.admin.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 관리자 신고 관리 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/report")
public class AdminReportController {

    private final AdminReportService adminReportService; //관리자 신고 관리 서비스

    /**
     * [컨트롤러]
     * 관리자가 신고 처리
     * 수락 / 거절
     * @param reportId 신고 아이디
     * @param requestDto 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> changeReportStatus(@PathVariable("id")Long reportId, @RequestBody AdminAcceptReportRequestDto requestDto) {

        adminReportService.changeReportStatus(reportId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","신고 처리 완료")));
    }

    /**
     * [컨트롤러]
     * 관리자가 신고 내역을 상세 조회
     * @param reportId 신고 아이디
     * @return ReportDetailsResponseDto 응답 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getReportDetails(@PathVariable("id")Long reportId) {

        return ResponseEntity.ok().body(ApiResponse.success(adminReportService.getReportDetails(reportId)));
    }


    /**
     * [컨트롤러]
     * 관리자 신고 목록 리스트 조회
     * @param status 신고 처리 상태
     * @param createdDateSortType 날짜 정렬 타입
     * @return PagedResponseDto<ReportListResponseDto> 응답 DTO
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllReports(@RequestParam(value = "status",required = false) ReportStatus status,
                                                        @RequestParam(value = "createdDateSortType") CreatedDateSortType createdDateSortType
                                                        ) {
        
        SearchReportCondDto condDto = SearchReportCondDto.create(status, createdDateSortType);
        PagedResponseDto<ReportListResponseDto> responseDtos = adminReportService.getAllReports(condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));


    }


}
