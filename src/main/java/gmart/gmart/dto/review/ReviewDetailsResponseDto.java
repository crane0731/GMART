package gmart.gmart.dto.review;

import gmart.gmart.domain.Review;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 응답 DTO
 */
@Getter
@Setter
public class ReviewDetailsResponseDto {

    public Long reviewId; //리뷰 아이디
    public String content; //리뷰 내용
    public Long rating; //리뷰 점수
    public String reviewType; //리뷰 타입
    public String createdAt; //생성일

    public Long reviewerId; //리뷰어 아이디
    public String reviewerNickname; //리뷰어 닉네임
    public String reviewerLoginId; //리뷰어 로그인 아이디

    public Long revieweeId; //리뷰 대상자 아이디
    public String revieweeNickname; //리뷰 대상자 닉네임
    public String revieweeLoginId; //리뷰 대상자 로그인 아이디

    public Long orderId; //주문 아이디
    public Long itemId; //상품 아이디
    public String itemTitle; //상품 제목
    public Long itemPrice; //상품 가격
    public Long deliveryPrice; //배송비


    /**
     * [생성 메서드]
     * @param review 리뷰 엔티티
     * @return ReviewResponseDto 응답 DTO
     */
    public static ReviewDetailsResponseDto create(Review review) {
        ReviewDetailsResponseDto dto = new ReviewDetailsResponseDto();

        dto.setReviewId(review.getId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setReviewType(review.getReviewType().toString());
        dto.setCreatedAt(DateFormatUtil.DateFormat(review.getCreatedDate()));

        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerNickname(review.getReviewer().getNickname());
        dto.setReviewerLoginId(review.getReviewer().getLoginId());

        dto.setRevieweeId(review.getReviewee().getId());
        dto.setRevieweeNickname(review.getReviewee().getNickname());
        dto.setRevieweeLoginId(review.getReviewee().getLoginId());

        dto.setOrderId(review.getOrder().getId());
        dto.setItemId(review.getOrder().getItem().getId());
        dto.setItemTitle(review.getOrder().getItem().getTitle());
        dto.setItemPrice(review.getOrder().getItemPrice());
        dto.setDeliveryPrice(review.getOrder().getDeliveryPrice());

        return dto;
    }

}
