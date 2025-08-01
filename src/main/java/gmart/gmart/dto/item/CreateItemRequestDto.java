package gmart.gmart.dto.item;

import gmart.gmart.domain.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 상품 생성 요청 DTO
 */
@Getter
@Setter
public class CreateItemRequestDto {

    @NotBlank(message = "상품 이름을 입력하세요.")
    private String title;//상품 이름

    @NotBlank(message = "상품 내용을 입력하세요.")
    private String content; //상품 내용

    @NotNull(message = "상품 가격을 입력하세요.")
    private Long itemPrice; //상품 가격

    @NotNull(message = "배송비 입력하세요.")
    private Long deliveryPrice; //배송비

    @NotNull(message = "거래장소를 입력하세요.")
    private String location; // 거래 장소

    private GundamGrade grade;//건담 등급

    private List<ItemImageRequestDto> itemImages; //상품 이미지 리스트
    private List<Long> gundamList;//건담 리스트

    @NotNull(message = "조립 상태를 입력하세요.")
    private AssemblyStatus assemblyStatus; //조립 상태

    @NotNull(message = "도색 상태를 입력하세요.")
    private PaintStatus paintStatus; //도색 상태

    @NotNull(message = "박스 상태를 입력하세요.")
    private BoxStatus boxStatus; //박스 상태



}
