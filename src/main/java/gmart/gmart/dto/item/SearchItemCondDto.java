package gmart.gmart.dto.item;

import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.enums.ItemSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 리스트 검색 조건 DTO
 */
@Getter
@Setter
public class SearchItemCondDto {

    private String title; //상품 이름으로 검색

    private Long gundamId; //건담아이디로 검색

    private GundamGrade gundamGrade; //건담 등급으로 검색
    private BoxStatus boxStatus;//박스 상태로 검색
    private PaintStatus paintStatus;//도색 상태로 검색
    private SaleStatus saleStatus;//판매 상태로 검색

    private ItemSortType sortType; //가격 높은순, 가격 낮은순, 조회수, 최신순


    /**
     * [생성 메서드]
     * @param title 상품 이름
     * @param gundamId 건담 아이디
     * @param gundamGrade 건담 등급
     * @param boxStatus 박스 상태
     * @param paintStatus 도색 상태
     * @param saleStatus 판매 상태
     * @param sortType 정렬 타입
     * @return
     */
    public static SearchItemCondDto create(String title, Long gundamId,GundamGrade gundamGrade, BoxStatus boxStatus, PaintStatus paintStatus, SaleStatus saleStatus, ItemSortType sortType) {
        SearchItemCondDto dto = new SearchItemCondDto();
        dto.title = title;
        dto.gundamId = gundamId;
        dto.gundamGrade = gundamGrade;
        dto.boxStatus = boxStatus;
        dto.paintStatus = paintStatus;
        dto.saleStatus = saleStatus;
        dto.sortType = sortType;
        return dto;
    }
}
