package gmart.gmart.dto.favorite;

import gmart.gmart.domain.FavoriteGundam;
import lombok.Getter;
import lombok.Setter;

/**
 * 관심 건담 리스트 응답 DTO
 */
@Getter
@Setter
public class FavoriteGundamListResponseDto {

    private String name; //이름

    private String grade; //등급

    private String imageUrl; // 이미지 URL


    /**
     * [생성 메서드]
     * @param favoriteGundam 관심 건담 엔티티
     * @return FavoriteGundamListResponseDto 응답 DTO
     */
    public static FavoriteGundamListResponseDto create(FavoriteGundam favoriteGundam) {
        FavoriteGundamListResponseDto dto = new FavoriteGundamListResponseDto();
        dto.setName(favoriteGundam.getGundam().getName());
        dto.setGrade(favoriteGundam.getGundam().getGrade().toString());
        dto.setImageUrl(favoriteGundam.getGundam().getImageUrl());
        return dto;
    }
}
