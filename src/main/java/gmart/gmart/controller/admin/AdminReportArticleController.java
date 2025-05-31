package gmart.gmart.controller.admin;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.reportarticle.ReportArticleDetailResponseDto;
import gmart.gmart.service.admin.AdminReportArticleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/report/article")
public class AdminReportArticleController {

    private final AdminReportArticleService adminReportArticleService;

    /**
     * [컨트롤러]
     * 관리자 - 게시글 신고 수락
     * @param reportArticleId 게시글 신고 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{reportArticleId}/accept")
    public ResponseEntity<ApiResponse<?>> acceptReportArticle(@PathVariable("reportArticleId") Long reportArticleId) {

        adminReportArticleService.acceptReport(reportArticleId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 신고 수락 완료")));
    }

    /**
     * [컨트롤러]
     * 관리자 - 게시글 신고 거절
     * @param reportArticleId 게시글 신고 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{reportArticleId}/reject")
    public ResponseEntity<ApiResponse<?>> rejectReportArticle(@PathVariable("reportArticleId") Long reportArticleId) {

        adminReportArticleService.rejectReport(reportArticleId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 신고 거절 완료")));
    }


    /**
     * [컨트롤러]
     * 관리자 - 게시글 신고 단일 상세 조회
     * @param reportArticleId 게시글 신고 아이디
     * @return  ReportArticleDetailResponseDto 응답 DTO
     */
    @GetMapping("/{reportArticleId}")
    public ResponseEntity<ApiResponse<?>> getReportArticleDetail(@PathVariable("reportArticleId") Long reportArticleId) {

        ReportArticleDetailResponseDto responseDto = adminReportArticleService.getReportArticleDetail(reportArticleId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }



}
