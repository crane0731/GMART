package gmart.gmart.dto.item;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.ItemImage;
import gmart.gmart.domain.enums.IsMain;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 이미지 응답 DTO
 */
@Getter
@Setter
public class ItemImageResponseDto {

    private String imageUrl; //이미지 URL
    private IsMain isMain; //대표 이미지 여부


    /**
     * [생성 메서드]
     * @param itemImage 상품 이미지 엔티티
     * @return ItemImageResponseDto 응답 DTO
     */
    public static ItemImageResponseDto create(ItemImage itemImage) {
        ItemImageResponseDto dto = new ItemImageResponseDto();
        dto.setImageUrl(itemImage.getImageUrl());
        dto.setIsMain(itemImage.getIsMain());
        return dto;
    }
}
