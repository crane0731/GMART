package gmart.gmart.dto.item;

import gmart.gmart.domain.enums.SaleStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 판매 상태 변경 DTO
 */
@Getter
@Setter
public class ChangeSaleStatusRequestDto {

    private SaleStatus saleStatus; //판매 상태

}
