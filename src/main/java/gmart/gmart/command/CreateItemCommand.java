package gmart.gmart.command;

import gmart.gmart.domain.enums.AssemblyStatus;
import gmart.gmart.domain.enums.BoxStatus;
import gmart.gmart.domain.enums.DealType;
import gmart.gmart.domain.enums.PaintStatus;
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

    private final String title;
    private final String content;
    private final Long itemPrice;
    private final Long deliveryPrice;
    private final String location;

    private final AssemblyStatus assemblyStatus;
    private final PaintStatus paintStatus;
    private final BoxStatus boxStatus;
    private final DealType dealType;

}
