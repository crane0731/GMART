package gmart.gmart.command;

import gmart.gmart.domain.enums.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Item 엔티티 객체를 수정 위해 필요한 비즈니스 데이터를 담는 Command 객체
 * API 요청 DTO와 분리하여 도메인 계층의 독립성을 유지하기 위해 사용
 */
@AllArgsConstructor
@Builder
@Getter
public class UpdateItemCommand {

    private String title;//상품 이름
    private String content;//상품 내용
    private Long itemPrice; //상품 가격
    private Long deliveryPrice; //배송비
    private String location; //거래 장소
    private GundamGrade gundamGrade;//건담 등급
    private AssemblyStatus assemblyStatus; //조립 상태
    private BoxStatus boxStatus; //박스 상태
    private PaintStatus paintStatus; //도색 상태
    private DealType dealType; //거래 타입
}
