package gmart.gmart.dto.review;

import gmart.gmart.domain.enums.ReviewType;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.enums.ReviewRole;
import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 조회 검색 조건 DTO
 */
@Getter
@Setter
public class SearchReviewCondDto {

    public ReviewRole reviewRole; //리뷰 역할
    public ReviewType reviewType; //리뷰 타입
    public CreatedDateSortType createdDateSortType; //생성일 정렬 타입

    /**
     * [생성 메서드]
     * @param reviewType 리뷰 타입
     * @param createdDateSortType // 생성일 정렬 타입
     * @return SearchReviewCondDto 검색 조건 DTO
     */
    public static SearchReviewCondDto create(ReviewRole reviewRole,ReviewType reviewType, CreatedDateSortType createdDateSortType) {
        SearchReviewCondDto searchReviewCondDto = new SearchReviewCondDto();
        searchReviewCondDto.reviewRole=reviewRole;
        searchReviewCondDto.reviewType = reviewType;
        searchReviewCondDto.createdDateSortType = createdDateSortType;
        return searchReviewCondDto;
    }


}
