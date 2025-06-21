package gmart.gmart.controller.review;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.review.CreateReviewRequestDto;
import gmart.gmart.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 리뷰 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/review")
public class ReviewController {

    private final ReviewService reviewService;// 리뷰 서비스

    /**
     * [컨트롤러]
     * 리뷰 등록
     * @param requestDto 리뷰 등록 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createReview(@Valid @RequestBody CreateReviewRequestDto requestDto, BindingResult bindingResult) {
        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        reviewService.createReview(requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","리뷰 등록 완료")));

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
