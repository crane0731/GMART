package gmart.gmart.dto.review;

import gmart.gmart.domain.Review;
import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 목록 응답 DTO
 */
@Getter
@Setter
public class ReviewListResponseDto {

    private Long reviewId; //리뷰 아이디

    private String reviewContent; //리뷰 내용
    private String reviewType; //리뷰 타입
    private Long rating; //리뷰 점수

    private Long orderId;//주문 아이디

    private Long itemId; //상품 아이디
    private String itemTitle; //상품 제목

    private Long reviewerId; //리뷰어 아이디
    private String reviewerNickName; //리뷰어 닉네임

    private Long revieweeId; //리뷰당사자 아이디
    private String revieweeNickName; //리뷰 당사자 닉네임

    /**
     * [생성 메서드]
     * @param review 리뷰 엔티티
     * @return ReviewListResponseDto 응답 DTO
     */
    public static ReviewListResponseDto create(Review review) {
        ReviewListResponseDto dto = new ReviewListResponseDto();

        dto.setReviewId(review.getId());
        dto.setReviewContent(review.getContent());
        dto.setReviewType(review.getReviewType().toString());
        dto.setRating(review.getRating());

        dto.setOrderId(review.getOrder().getId());

        dto.setItemId(review.getOrder().getItem().getId());
        dto.setItemTitle(review.getOrder().getItem().getTitle());

        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerNickName(review.getReviewer().getNickname());

        dto.setRevieweeId(review.getReviewee().getId());
        dto.setRevieweeNickName(review.getReviewee().getNickname());

        return dto;
    }


}
