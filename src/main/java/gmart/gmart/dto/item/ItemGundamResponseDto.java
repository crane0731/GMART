package gmart.gmart.dto.item;

import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.ItemGundam;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 건담 응답 DTO
 */
@Getter
@Setter
public class ItemGundamResponseDto {

    private Long gundamId;//건담 아이디

    private String name; //이름

    private String grade; //등급

    private String imageUrl; // 이미지 URL


    /**
     * 생성 메서드
     * @param itemGundam 상품 건담 엔티티
     * @return ItemGundamResponseDto 응답 DTO
     */
    public static ItemGundamResponseDto createDto(ItemGundam itemGundam) {
        ItemGundamResponseDto dto = new ItemGundamResponseDto();
        dto.setGundamId(itemGundam.getGundam().getId());
        dto.setName(itemGundam.getGundam().getName());
        dto.setGrade(itemGundam.getGundam().getGrade().toString());
        dto.setImageUrl(itemGundam.getGundam().getImageUrl());
        return dto;
    }
}
