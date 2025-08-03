package gmart.gmart.dto.item;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.ItemImage;
import gmart.gmart.domain.enums.IsMain;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 상품 리스트 조회 DTO
 */
@Getter
@Setter
public class ItemListResponseDto {

    public Long itemId;//상품 아이디
    public String itemTitle; //상품 제목
    public Long itemPrice; //상품 가격
    public Long deliveryPrice; // 배송비
    public String itemMainImageUrl; //상품 메인 이미지 URL
    public String saleStatus;//판매 상태
    public String createdAt; //상품 등록일

    /**
     * [생성 메서드]
     * @param item 상품
     * @return ItemListResponseDto 응답 DTO
     */
    public static ItemListResponseDto create(Item item){
        ItemListResponseDto dto = new ItemListResponseDto();
        dto.setItemId(item.getId());
        dto.setItemTitle(item.getTitle());
        dto.setItemPrice(item.getItemPrice());
        dto.setDeliveryPrice(item.getDeliveryPrice());


        //메인 이미지 세팅
        List<ItemImage> itemImages = item.getItemImages();
        setMainImageUrl(itemImages, dto);

        dto.setSaleStatus(item.getSaleStatus().toString());

        dto.setCreatedAt(DateFormatUtil.DateFormat(item.getCreatedDate()));

        return dto;
    }

    //==메인 상품 이미지 URL 세팅 ==//
    private static void setMainImageUrl(List<ItemImage> itemImages, ItemListResponseDto dto) {
        for (ItemImage itemImage : itemImages) {
            if(itemImage.getIsMain().equals(IsMain.MAIN)){
                dto.setItemMainImageUrl(itemImage.getImageUrl());
            }
        }
    }


}
