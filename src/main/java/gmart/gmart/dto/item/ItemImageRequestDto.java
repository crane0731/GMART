package gmart.gmart.dto.item;

import gmart.gmart.domain.enums.IsMain;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 상품 이미지 요청 DTO
 */
@Getter
@Setter
public class ItemImageRequestDto {

    private String imageUrl;
    private IsMain isMain;
}
