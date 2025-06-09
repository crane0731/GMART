package gmart.gmart.controller;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.item.ChangeSaleStatusRequestDto;
import gmart.gmart.dto.item.CreateItemRequestDto;
import gmart.gmart.dto.item.UpdateItemRequestDto;
import gmart.gmart.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
