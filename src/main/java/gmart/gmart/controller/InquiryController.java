package gmart.gmart.controller;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.inquiry.*;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.InquiryService;
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
     * 문의 등록 컨트롤러
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
     * 문의 수정 컨트롤러
     * @param inquiryId 문의 아이디(PK)
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러메시지를 담을 객체
     * @return 성공 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateInquiry(@PathVariable("id") Long inquiryId,@Valid @RequestBody UpdateInquiryRequestDto requestDto,BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        inquiryService.updateInquiry(inquiryId,requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","문의 수정 완료")));

    }

    /**
     * 문의 삭제 컨트롤러
     * @param inquiryId 문의 아이디(PK)
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteInquiry(@PathVariable("id") Long inquiryId) {

        inquiryService.deleteInquiry(inquiryId);

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
     * 조건에 따른 문의 리스트 조회
     * @param cond 검색 조건
     * @return Page<InquiryListResponseDto> 응답 DTO
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllInquiry(@RequestBody SearchInquiryCondDto cond) {

        PagedResponseDto<InquiryListResponseDto> responseDto = inquiryService.getAllInquiry(cond);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }

    /**
     * 문의 답변(관리자)
     */

    /**
     * 문의 삭제(관리자)
     */


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
