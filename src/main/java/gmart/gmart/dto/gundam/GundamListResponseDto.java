package gmart.gmart.dto.gundam;

import gmart.gmart.domain.Gundam;
import lombok.Getter;
import lombok.Setter;

/**
 * 건담 리스트 응답 DTO
 */
@Getter
@Setter
public class GundamListResponseDto {

    private String name; //이름

    private String grade; //등급

    private String imageUrl; // 이미지 URL


    /**
     * [생성 메서드]
     * @param gundam 건담 엔티티
     * @return GundamListResponseDto 응답 DTO
     */
    public static GundamListResponseDto createDto(Gundam gundam) {
        GundamListResponseDto dto = new GundamListResponseDto();
        dto.setName(gundam.getName());
        dto.setGrade(gundam.getGrade().toString());
        dto.setImageUrl(gundam.getImageUrl());
        return dto;
    }

}
