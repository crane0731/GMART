package gmart.gmart.controller;


import gmart.gmart.domain.Inquiry;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.inquiry.CreateInquiryRequestDto;
import gmart.gmart.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 문의 조회
     */

    /**
     * 문의 리스트 조회
     */

    /**
     * 문의 수정
     */

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
