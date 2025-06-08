package gmart.gmart.dto.store;

import gmart.gmart.domain.Store;
import lombok.Getter;
import lombok.Setter;

/**
 * 상점 리스트 조회 응답 DTO
 */
@Getter
@Setter
public class StoreListResponseDto {

    private Long storeId; //상점 아이디
    private String storeName; //상점 이름
    private String storeProfileImageUrl; //상점 이미지

    private Long rating; //상점 점수
    private Long reviewedCount; //리뷰 받은 수


    /**
     * [생성 메서드]
     * @param store 상점 엔티티
     * @return StoreListResponseDto 응답 DTO
     */
    public static StoreListResponseDto create(Store store) {
        StoreListResponseDto dto = new StoreListResponseDto();
        dto.setStoreId(store.getId());
        dto.setStoreName(store.getName());
        dto.setStoreProfileImageUrl(store.getStoreProfileImage().getImageUrl());

        dto.setRating(store.getRating());
        dto.setReviewedCount(store.getReviewedCount());

        return dto;
    }
}
