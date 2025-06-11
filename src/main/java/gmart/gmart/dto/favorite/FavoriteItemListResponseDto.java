package gmart.gmart.dto.favorite;

import gmart.gmart.domain.FavoriteItem;
import gmart.gmart.domain.ItemImage;
import gmart.gmart.domain.enums.IsMain;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 관심 상품 리스트 조회 응답 DTO
 */
@Getter
@Setter
public class FavoriteItemListResponseDto {

    private Long favoriteItemId;//관심 상품 아이디

    private Long memberId; //회원 아이디
    private String memberNickname; //회원 닉네임
    private String memberLoginId; //회원 로그인 아이디

    private Long itemId; //상품 아이디
    private String itemTitle; //상품 이름
    private String itemMainImageUrl;//상품 메인 이미지
    private Long itemPrice; //상품 가격
    private Long deliveryPrice; //배송비


    /**
     * [생성 메서드]
     * @param favoriteItem 관심 상품 엔티티
     * @return FavoriteItemListResponseDto 응답 DTO
     */
    public static FavoriteItemListResponseDto create(FavoriteItem favoriteItem) {

        FavoriteItemListResponseDto dto = new FavoriteItemListResponseDto();

        dto.setFavoriteItemId(favoriteItem.getId());

        dto.setMemberId(favoriteItem.getMember().getId());
        dto.setMemberNickname(favoriteItem.getMember().getNickname());
        dto.setMemberLoginId(favoriteItem.getMember().getLoginId());

        dto.setItemId(favoriteItem.getItem().getId());
        dto.setItemTitle(favoriteItem.getItem().getTitle());

        List<ItemImage> itemImages = favoriteItem.getItem().getItemImages();

        settingItemMainImageUrl(itemImages, dto);

        dto.setItemPrice(favoriteItem.getItem().getItemPrice());
        dto.setDeliveryPrice(favoriteItem.getItem().getDeliveryPrice());
        return dto;
    }



    //==상품 메인 이미지 세팅==//
    private static void settingItemMainImageUrl(List<ItemImage> itemImages, FavoriteItemListResponseDto dto) {
        for (ItemImage itemImage : itemImages) {
            if(itemImage.getIsMain().equals(IsMain.MAIN));
            dto.itemMainImageUrl=itemImage.getImageUrl();
        }
    }
}
