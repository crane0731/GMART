package gmart.gmart.dto.image;

import lombok.Getter;
import lombok.Setter;

/**
 * 이미지 URL 응답 DTO
 */
@Getter
@Setter
public class ImageUrlResponseDto {

    private String imageUrl;


    /**
     * DTO 생성 메서드
     */
    public static ImageUrlResponseDto createDto(String imageUrl) {
        ImageUrlResponseDto dto = new ImageUrlResponseDto();
        dto.imageUrl = imageUrl;
        return dto;
    }

}
