package gmart.gmart.controller.inquiry;

import gmart.gmart.domain.enums.AnswerStatus;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.inquiry.*;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.inquiry.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 문의 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/gmart/inquiry")
public class InquiryController {

    private final InquiryService inquiryService; //문의 서비스

    /**
     * [컨트롤러]
     * 문의 등록
     * @param requestDto 등록 요청 DTO
     * @param bindingResult 에러메시지를 담을 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createInquiry(@Valid @RequestBody CreateInquiryRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //문의 등록
        inquiryService.createInquiry(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","문의 등록 완료")));

    }

    /**
     * [컨트롤러]
     * 문의 논리적 삭제 처리
     * @param inquiryId 문의 아이디(PK)
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> softDelete(@PathVariable("id") Long inquiryId) {

        inquiryService.softDelete(inquiryId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","문의 삭제 완료")));

    }

    /**
     * 문의 상세 조회
     * @param inquiryId 문의 아이디
     * @return InquiryResponseDto 응답 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getInquiry(@PathVariable("id") Long inquiryId) {

        InquiryResponseDto responseDto = inquiryService.getInquiry(inquiryId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }


    /**
     * [컨트롤러]
     * 조건에 따른 문의 목록 조회
     * @param title 문의 제목
     * @param answerStatus 답변 상태
     * @param createdDateSortType 날짜 정렬 타입
     * @return Page<InquiryListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllInquiry(@RequestParam(value = "title", required = false)String title,
                                                        @RequestParam(value = "answerStatus",required = false)AnswerStatus answerStatus,
                                                        @RequestParam(value = "createdDateSortType",required = false)CreatedDateSortType createdDateSortType,
                                                        @RequestParam(value = "page", defaultValue = "0") int page) {


        SearchInquiryCondDto condDto = SearchInquiryCondDto.create(title, answerStatus, createdDateSortType);

        PagedResponseDto<InquiryListResponseDto> responseDto = inquiryService.getAllInquiry(condDto,page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }

    //==필드에러가 있는지 확인하는 로직==//
    private boolean errorCheck(BindingResult bindingResult, Map<String, String> errorMessages) {
        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );

            return true;
        }
        return false;
    }
}
