package gmart.gmart.dto.store;

import gmart.gmart.domain.LikeStore;
import gmart.gmart.dto.favorite.FavoriteStoreListResponseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 좋아요 누른 상점 리스트 조회 응답 DTO
 */
@Getter
@Setter
public class LikeStoreListResponseDto {

    private Long likeStoreId;//관심 상점 아이디

    private Long memberId; //회원 아이디
    private String memberNickname; //회원 닉네임
    private String memberLoginId; //회원 로그인 아이디

    private Long storeId; //상점 아이디
    private String storeName; //상점 이름
    private String storeProfileImageUrl;//상점 프로필 이미지
    private Long storeItemCount; // 상점에 등록된 상품 수

    /**
     * [생성 메서드]
     * @param likeStore 상점 좋아요 엔티티
     * @return LikeStoreListResponseDto 응답 DTO
     */
    public static LikeStoreListResponseDto create(LikeStore likeStore) {
        LikeStoreListResponseDto dto = new LikeStoreListResponseDto();

        dto.setLikeStoreId(likeStore.getId());

        dto.setMemberId(likeStore.getMember().getId());
        dto.setMemberNickname(likeStore.getMember().getNickname());
        dto.setMemberLoginId(likeStore.getMember().getLoginId());

        dto.setStoreId(likeStore.getStore().getId());
        dto.setStoreName(likeStore.getStore().getName());
        dto.setStoreProfileImageUrl(likeStore.getStore().getStoreProfileImage().getImageUrl());
        dto.setStoreItemCount(likeStore.getStore().getItemCount());

        return dto;
    }


}
