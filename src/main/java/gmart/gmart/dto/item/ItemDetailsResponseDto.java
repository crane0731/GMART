package gmart.gmart.dto.item;

import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.Item;
import gmart.gmart.domain.ItemGundam;
import gmart.gmart.domain.ItemImage;
import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 상세조회 응답 DTO
 */
@Getter
@Setter
public class ItemDetailsResponseDto {

    private Long memberId;//판매자 아이디

    private Long storeId;// 상점 아이디
    private String storeName;// 상점 이름
    private String storeProfileImage;//상점 프로필 이미지
    private Long itemCount; //상점에 등록된 상품 수
    private Long likedCount; //상점 좋아요 수


    private Long itemId;//아이템 아이디
    private String itemTitle; //상품 제목
    private String itemContent;//상품 소개
    private String location;//거래 장소
    private Long itemPrice; //상품 가격
    private Long deliveryPrice;//배송비

    private Long viewCount;//조회수
    private Long favoriteCount;//찜한 수
    private Long chattingCount;//채팅 수
    private Long reportedCount;//신고 받은 수

    private String saleStatus;//판매 상태
    private String assemblyStatus;//조립 상태
    private String boxStatus;//박스 상태
    private String paintStatus;//도색 상태
    private String reportedStatus;//신고 상태
    private String dealType;//거래 타입

    private String createdAt; //생성일
    private String updatedAt; //수정일

    private List<ItemImageResponseDto> itemImages=new ArrayList<>();//상품 이미지들
    private List<ItemGundamResponseDto> gundams=new ArrayList<>(); //건담들


    /**
     * [생성 메서드]
     * @param item 상품 엔티티
     * @return ItemDetailsResponseDto 응답 DTO
     */
    public static ItemDetailsResponseDto create(Item item) {

        ItemDetailsResponseDto dto = new ItemDetailsResponseDto();

        dto.setMemberId(item.getStore().getMember().getId());
        dto.setStoreId(item.getStore().getId());
        dto.setStoreName(item.getStore().getName());
        dto.setStoreProfileImage(item.getStore().getStoreProfileImage().getImageUrl());
        dto.setItemCount(item.getStore().getItemCount());
        dto.setLikedCount(item.getStore().getLikedCount());

        dto.setItemId(item.getId());
        dto.setItemTitle(item.getTitle());
        dto.setItemContent(item.getContent());
        dto.setItemPrice(item.getItemPrice());
        dto.setDeliveryPrice(item.getDeliveryPrice());

        dto.setViewCount(item.getViewCount());
        dto.setFavoriteCount(item.getFavoriteCount());
        dto.setReportedCount(item.getReportedCount());

        dto.setSaleStatus(item.getSaleStatus().toString());
        dto.setAssemblyStatus(item.getAssemblyStatus().toString());
        dto.setBoxStatus(item.getBoxStatus().toString());
        dto.setPaintStatus(item.getPaintStatus().toString());
        dto.setReportedStatus(item.getReportedStatus().toString());

        dto.setCreatedAt(DateFormatUtil.DateFormat(item.getCreatedDate()));
        dto.setUpdatedAt(DateFormatUtil.DateFormat(item.getUpdatedDate()));

        //건담 목록 세팅
        List<ItemImage> itemImages = item.getItemImages();
        addItemImages(itemImages, dto);

        //상품 이미지 목록 세팅
        List<ItemGundam> itemGundams = item.getItemGundams();
        addGundams(itemGundams, dto);

        return dto;

    }
    //==DTO 에 건담목록 세팅==//
    private static void addGundams(List<ItemGundam> itemGundams, ItemDetailsResponseDto dto) {
        for (ItemGundam itemGundam : itemGundams) {

            ItemGundamResponseDto itemGundamResponseDto = ItemGundamResponseDto.createDto(itemGundam);

            dto.getGundams().add(itemGundamResponseDto);
        }
    }

    //==DTO 에 상품 이미지 세팅==//
    private static void addItemImages(List<ItemImage> itemImages, ItemDetailsResponseDto dto) {
        for (ItemImage itemImage : itemImages) {
            ItemImageResponseDto itemImageResponseDto = ItemImageResponseDto.create(itemImage);
            dto.getItemImages().add(itemImageResponseDto);
        }
    }

}
