package gmart.gmart.controller.admin;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.inquiry.InquiryAnswerRequestDto;
import gmart.gmart.service.admin.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 관리자용 문의 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/inquiry")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;//관리자 문의 서비스

    /**
     * 관리자 문의 답변
     * @param inquiryId 문의 아이디(PK)
     * @param requestDto 문의 답변 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<ApiResponse<?>> createAnswer(@PathVariable("id") Long inquiryId, @RequestBody InquiryAnswerRequestDto requestDto) {

        adminInquiryService.createAnswer(inquiryId, requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "답변 등록 성공")));

    }

    /**
     * 관리자 문의 답변 삭제
     * @param inquiryId 문의 아이디(PK)
     * @return  성공 메시지
     */
    @DeleteMapping("/{id}/answer")
    public ResponseEntity<ApiResponse<?>> deleteAnswer(@PathVariable("id") Long inquiryId) {

        adminInquiryService.deleteAnswer(inquiryId);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "답변 삭제 성공")));

    }




    /**
     * 관리자 문의 삭제 컨트롤러
     * @param inquiryId 문의 아이디(PK)
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> adminDeleteInquiry(@PathVariable("id") Long inquiryId) {

        adminInquiryService.deleteInquiry(inquiryId);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "문의 삭제 성공")));

    }

}
