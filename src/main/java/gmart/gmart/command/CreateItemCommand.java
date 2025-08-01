package gmart.gmart.command;

import gmart.gmart.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Item 엔티티 객체를 생성하기 위해 필요한 비즈니스 데이터를 담는 Command 객체
 * API 요청 DTO와 분리하여 도메인 계층의 독립성을 유지하기 위해 사용
 */
@AllArgsConstructor
@Builder
@Getter
public class CreateItemCommand {

    private  String title;
    private  String content;
    private  Long itemPrice;
    private  Long deliveryPrice;
    private  String location;
    private GundamGrade gundamGrade;

    private  AssemblyStatus assemblyStatus;
    private  PaintStatus paintStatus;
    private  BoxStatus boxStatus;

}
