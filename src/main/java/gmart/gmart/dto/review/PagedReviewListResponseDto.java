package gmart.gmart.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 페이징 리뷰 목록 응답 DTO
 */
@Getter
@Setter
public class PagedReviewListResponseDto {


    private Long goodCount; //좋아요 수
    private Long badCount; //별로에요 수
    private Long ratingAverage; //점수 평균

    private List<ReviewListResponseDto> reviewList; //리뷰 응답 DTO 리스트

    private int page;            // 현재 페이지 번호 (0부터 시작)
    private int size;            // 페이지당 개수
    private int totalPages;      // 전체 페이지 수
    private long totalElements;  // 전체 데이터 수
    private boolean first;       // 첫 페이지 여부
    private boolean last;        // 마지막 페이지 여부


    /**
     * [생성 메서드]
     * @param goodCount 좋아요 수
     * @param badCount 별로에요 수
     * @param ratingAverage 점수 평균
     * @param reviewList 리뷰 응답 DTO 리스트
     * @param page 현재 페이지 번호
     * @param size 페이지당 개수
     * @param totalPages 전체 페이지 수
     * @param totalElements 전체 데이터 수
     * @param first 첫 페이지 여부
     * @param last 마지막 페이지 여부
     * @return PagedReviewListResponseDto
     */
    public static PagedReviewListResponseDto create(Long goodCount, Long badCount, Long ratingAverage, List<ReviewListResponseDto> reviewList,
                                                    int page, int size, int totalPages, long totalElements, boolean first, boolean last) {
        PagedReviewListResponseDto dto = new PagedReviewListResponseDto();
        dto.setGoodCount(goodCount);
        dto.setBadCount(badCount);
        dto.setRatingAverage(ratingAverage);
        dto.setReviewList(reviewList);
        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalElements(totalElements);
        dto.setFirst(first);
        dto.setLast(last);
        return dto;

    }

}
