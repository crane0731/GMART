package gmart.gmart.dto.favorite;

import gmart.gmart.domain.FavoriteStore;
import lombok.Getter;
import lombok.Setter;

/**
 * 관심 상점 리스트 응답 DTO
 */
@Getter
@Setter
public class FavoriteStoreListResponseDto {


    private Long favoriteStoreId;//관심 상점 아이디

    private Long storeId; //상점 아이디
    private String storeName; //상점 이름
    private String storeProfileImageUrl;//상점 프로필 이미지
    private Long storeItemCount; // 삼점에 등록된 상품 수


    /**
     * [생성 메서드]
     * @param favoriteStore 관심 상점 엔티티
     * @return FavoriteStoreListResponseDto 응답 DTO
     */
    public static FavoriteStoreListResponseDto create(FavoriteStore favoriteStore) {
        FavoriteStoreListResponseDto dto = new FavoriteStoreListResponseDto();
        dto.setFavoriteStoreId(favoriteStore.getId());
        dto.setStoreId(favoriteStore.getStore().getId());
        dto.setStoreName(favoriteStore.getStore().getName());
        dto.setStoreProfileImageUrl(favoriteStore.getStore().getStoreProfileImage().getImageUrl());
        dto.setStoreItemCount(favoriteStore.getStore().getItemCount());
        return dto;
    }
}
