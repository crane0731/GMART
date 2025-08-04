package gmart.gmart.dto.store;

import gmart.gmart.domain.Store;
import lombok.Getter;
import lombok.Setter;

/**
 * 상점 정보 상세 조회 응답 DTO
 */
@Getter
@Setter
public class StoreResponseDto {

    private Long memberId;
    private String memberLoginId;
    private String memberNickname;
    private String memberPhone;
    private String memberRole;
    private String memberProfileImageUrl;
    private Long memberSuspensionCount;


    private Long storeId;
    private String storeName;
    private String introduction;
    private String storeProfileImageUrl;
    private String storeStatus;
    private Long itemCount;
    private Long totalVisitCount;
    private Long reviewedCount;
    private Long likedCount;
    private Long favoriteCount;
    private Long tradeCount;


    /**
     * [생성 메서드]
     * @param store 상점 엔티티
     * @return StoreResponseDto 응답 DTO
     */
    public static StoreResponseDto create(Store store) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.memberId = store.getMember().getId();
        dto.memberLoginId = store.getMember().getLoginId();
        dto.memberNickname = store.getMember().getNickname();
        dto.memberPhone = store.getMember().getPhoneNumber();
        dto.memberRole=store.getMember().getMemberRole().toString();
        dto.memberProfileImageUrl=store.getMember().getProfileImageUrl();
        dto.memberSuspensionCount=store.getMember().getSuspensionCount();

        dto.storeId = store.getId();
        dto.storeName = store.getName();
        dto.introduction = store.getIntroduction();
        dto.storeProfileImageUrl=store.getStoreProfileImage().getImageUrl();
        dto.storeStatus=store.getStatus().toString();
        dto.itemCount=store.getItemCount();
        dto.totalVisitCount=store.getTotalVisitCount();
        dto.reviewedCount=store.getReviewedCount();
        dto.likedCount=store.getLikedCount();
        dto.favoriteCount=store.getFavoriteCount();
        dto.tradeCount = store.getTradeCount();

        return dto;


    }

}
