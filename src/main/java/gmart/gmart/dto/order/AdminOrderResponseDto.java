package gmart.gmart.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 관리자 주문 응답 DTO
 */
@Getter
@Setter
public class AdminOrderResponseDto {

    private Long totalTradeCount; //전체 거래 량
    private Long totalGMoneyAmount; //전체 건머니 거래량
    private Long totalPaidGMoneyAmount;//실제 지불된 전체 건머니 거래량
    private Long totalUsedGPointAmount;//사용된 건포인트 거래량


    private int page;            // 현재 페이지 번호 (0부터 시작)
    private int size;            // 페이지당 개수
    private int totalPages;      // 전체 페이지 수
    private long totalElements;  // 전체 데이터 수
    private boolean first;       // 첫 페이지 여부
    private boolean last;        // 마지막 페이지 여부

    private List<AdminOrderListResponseDto> list;

    /**
     * [생성 메서드]
     * @param totalTradeCount 전체 거래 량
     * @param totalGMoneyAmount 전체 건머니 거래량
     * @param totalPaidGMoneyAmount 실제 지불된 전체 건머니 거래량
     * @param totalUsedGPointAmount 사용된 건포인트 거래량
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지당 개수
     * @param totalPages 전체 페이지 수
     * @param totalElements 전체 데이터 수
     * @param first 첫 페이지 여부
     * @param last 마지막 페이지 여부
     * @param list 관리자 주문 응답 DTO 리스트
     * @return AdminOrderResponseDto
     */
    public static AdminOrderResponseDto create(Long totalTradeCount, Long totalGMoneyAmount, Long totalPaidGMoneyAmount, Long totalUsedGPointAmount,
                                               int page, int size, int totalPages, long totalElements, boolean first, boolean last,
                                               List<AdminOrderListResponseDto> list) {
        AdminOrderResponseDto dto = new AdminOrderResponseDto();
        dto.setTotalTradeCount(totalTradeCount);
        dto.setTotalGMoneyAmount(totalGMoneyAmount);
        dto.setTotalPaidGMoneyAmount(totalPaidGMoneyAmount);
        dto.setTotalUsedGPointAmount(totalUsedGPointAmount);
        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalPages(totalPages);
        dto.setTotalElements(totalElements);
        dto.setFirst(first);
        dto.setLast(last);
        dto.setList(list);
        return dto;
    }
}
