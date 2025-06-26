package gmart.gmart.controller.item;

import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.ItemSortType;
import gmart.gmart.dto.item.*;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/item")
public class ItemController {

    private final ItemService itemService; //상품 서비스

    /**
     * [컨트롤러]
     * 상품 생성
     * @param requestDto 요청 DTO
     * @param bindingResult  에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createItem( @Valid @RequestBody CreateItemRequestDto requestDto, BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        itemService.createItem(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상품 등록 성공")));
    }

    /**
     * [컨트롤러]
     * 상품 삭제
     * @param itemId 상품 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteItem(@PathVariable("id")Long itemId) {

        itemService.deleteItem(itemId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상품 삭제 성공")));
    }

    /**
     * [컨트롤러]
     * 상품 판매상태 변경
     * @param itemId 상품 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>>saleStatus(@PathVariable("id")Long itemId, @RequestBody ChangeSaleStatusRequestDto requestDto){

        itemService.changeSaleStatus(itemId,requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상품 판매중")));
    }


    /**
     * [컨트롤러]
     * 상품 업데이트
     * @param itemId 상품 아이디
     * @param requestDto 상품 업데이트 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateItem(@PathVariable("id")Long itemId, @Valid @RequestBody UpdateItemRequestDto requestDto, BindingResult bindingResult ) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        itemService.updateItem(itemId,requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상품 업데이트 성공")));
    }

    /**
     * [컨트롤러]
     * 상품 정보 상세 조회
     * @param itemId 상품 아이디
     * @return ItemDetailsResponseDto 응답 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getItemDetails(@PathVariable("id")Long itemId) {

        ItemDetailsResponseDto responseDto = itemService.getItemDetails(itemId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }

    /**
     * [컨트롤러]
     * 검색 조건에 따라 상품 목록 조회
     * @param title 상품 이름
     * @param gundamId 건담 아이디
     * @param gundamGrade 건담 등급
     * @param boxStatus 박스 상태
     * @param paintStatus 도색 상태
     * @param dealType 거래 타입
     * @param saleStatus 판매 상태
     * @param sortType 정렬 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<ItemListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllItems(@RequestParam(value = "title",required = false) String title,
                                                      @RequestParam(value = "gundamId",required = false) Long gundamId,
                                                      @RequestParam(value = "gundamGrade",required = false) GundamGrade gundamGrade,
                                                      @RequestParam(value = "boxStatus",required = false) BoxStatus boxStatus,
                                                      @RequestParam(value = "paintStatus",required = false)PaintStatus paintStatus,
                                                      @RequestParam(value = "dealType",required = false)DealType dealType,
                                                      @RequestParam(value = "saleStatus",required = false) SaleStatus saleStatus,
                                                      @RequestParam(value = "sortType",required = false) ItemSortType sortType,
                                                      @RequestParam(value = "page", defaultValue = "0") int page
                                                      ) {

        SearchItemCondDto condDto = SearchItemCondDto.create(title, gundamId, gundamGrade, boxStatus, paintStatus, dealType, saleStatus, sortType);
        PagedResponseDto<ItemListResponseDto> responseDto = itemService.getAllItems(condDto, page);
        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }

    /**
     * [컨트롤러]
     * 검색 조건에 따라 자신의 상품 목록 조회
     * @param title 상품 이름
     * @param gundamId 건담 아이디
     * @param gundamGrade 건담 등급
     * @param boxStatus 박스 상태
     * @param paintStatus 도색 상태
     * @param dealType 거래 타입
     * @param saleStatus 판매 상태
     * @param sortType 정렬 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<ItemListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getAllMyItems(@RequestParam(value = "title",required = false) String title,
                                                      @RequestParam(value = "gundamId",required = false) Long gundamId,
                                                      @RequestParam(value = "gundamGrade",required = false) GundamGrade gundamGrade,
                                                      @RequestParam(value = "boxStatus",required = false) BoxStatus boxStatus,
                                                      @RequestParam(value = "paintStatus",required = false)PaintStatus paintStatus,
                                                      @RequestParam(value = "dealType",required = false)DealType dealType,
                                                      @RequestParam(value = "saleStatus",required = false) SaleStatus saleStatus,
                                                      @RequestParam(value = "sortType",required = false) ItemSortType sortType,
                                                      @RequestParam(value = "page", defaultValue = "0") int page
    ) {

        SearchItemCondDto condDto = SearchItemCondDto.create(title, gundamId, gundamGrade, boxStatus, paintStatus, dealType, saleStatus, sortType);
        PagedResponseDto<ItemListResponseDto> responseDto = itemService.getAllMyItems(condDto, page);
        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }





    //==필드에러가 있는지 확인하는 로직==//
    private boolean errorCheck(BindingResult bindingResult, Map<String, String> errorMessages) {
        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );

            return true;
        }
        return false;
    }
}
