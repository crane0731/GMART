package gmart.gmart.controller.review;

import gmart.gmart.domain.enums.ReviewType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.enums.ReviewRole;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.review.CreateReviewRequestDto;
import gmart.gmart.dto.review.ReviewDetailsResponseDto;
import gmart.gmart.dto.review.ReviewListResponseDto;
import gmart.gmart.dto.review.SearchReviewCondDto;
import gmart.gmart.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 리뷰 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart")
public class ReviewController {

    private final ReviewService reviewService;// 리뷰 서비스

    /**
     * [컨트롤러]
     * 리뷰 등록
     * @param requestDto 리뷰 등록 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/review")
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

    /**
     * [컨트롤러]
     * aaaaa
     * @param reviewId 리뷰 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/review/{id}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable("id") Long reviewId) {

        reviewService.softDelete(reviewId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","리뷰 삭제 완료")));

    }

    /**
     * [컨트롤러]
     * 리뷰 상세 조회 (단건 조회)
     * @param reviewId 리뷰 아이디
     * @return ReviewResponseDto 응답 DTO
     */
    @GetMapping("/review/{id}")
    public ResponseEntity<ApiResponse<?>>  getReviewDetails(@PathVariable("id") Long reviewId) {

        ReviewDetailsResponseDto responseDto = reviewService.getReviewDetails(reviewId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    /**
     * [컨트롤러]
     * 회원이 자신의 리뷰 목록을 조건에 따라 조회
     * @param reviewRole 리뷰 역할
     * @param reviewType 리뷰 타입
     * @param createdDateSortType 날짜 정렬 타입
     * @return PagedResponseDto<ReviewListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/members/me/review")
    public ResponseEntity<ApiResponse<?>> getMyReviews(@RequestParam("reviewRole") ReviewRole reviewRole,
                                                       @RequestParam("reviewType") ReviewType reviewType,
                                                       @RequestParam("createdDateSortType") CreatedDateSortType createdDateSortType) {

        SearchReviewCondDto condDto = SearchReviewCondDto.create(reviewRole, reviewType, createdDateSortType);

        PagedResponseDto<ReviewListResponseDto> responseDtos = reviewService.getMyReviews(condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
    }

    /**
     * [컨트롤러]
     * 특정 회원의 리뷰 목록을 조건에 따라 조회
     * @param memberId 회원 아이디
     * @param reviewRole 리뷰 역할
     * @param reviewType 리뷰 타입
     * @param createdDateSortType 날짜 정렬 타입
     * @return PagedResponseDto<ReviewListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/members/{id}/review")
    public ResponseEntity<ApiResponse<?>> getReviews(@PathVariable("id")Long memberId,
                                                     @RequestParam("reviewRole") ReviewRole reviewRole,
                                                     @RequestParam("reviewType") ReviewType reviewType,
                                                     @RequestParam("createdDateSortType") CreatedDateSortType createdDateSortType) {

        SearchReviewCondDto condDto = SearchReviewCondDto.create(reviewRole, reviewType, createdDateSortType);

        PagedResponseDto<ReviewListResponseDto> responseDtos = reviewService.getReviews(memberId,condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
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
