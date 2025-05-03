package gmart.gmart.dto.image;

import lombok.Getter;
import lombok.Setter;

/**
 * 이미지 URL 응답 DTO
 */
@Getter
@Setter
public class ProfileImageUrlResponseDto {

    private String imageUrl;


    /**
     * DTO 생성 메서드
     */
    public static ProfileImageUrlResponseDto createDto(String imageUrl) {
        ProfileImageUrlResponseDto dto = new ProfileImageUrlResponseDto();
        dto.imageUrl = imageUrl;
        return dto;
    }

}
