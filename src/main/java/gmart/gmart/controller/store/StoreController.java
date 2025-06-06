package gmart.gmart.controller.store;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.store.CreateStoreRequestDto;
import gmart.gmart.dto.store.StoreResponseDto;
import gmart.gmart.dto.store.UpdateStoreRequestDto;
import gmart.gmart.service.store.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 상점 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/store")
public class StoreController {

    private final StoreService storeService; //상점 서비스

    /**
     * [컨트롤러]
     * 상점 생성
     * @param requestDto 상품 생성 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createStore(@Valid @RequestBody CreateStoreRequestDto requestDto,BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        storeService.createStore(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상점 생성 완료")));


    }

    /**
     * [컨트롤러]
     * 상점 업데이트
     * @param storeId 상점 아이디
     * @param requestDto 상점 수정 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateStore(@PathVariable("id") Long storeId, @Valid @RequestBody UpdateStoreRequestDto requestDto, BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        storeService.updateStore(storeId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상점 수정 완료")));

    }

    /**
     * [컨트롤러]
     * 상점 정보 상세 조회
     * @param storeId 상점 아이디
     * @return StoreResponseDto 응답 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getStoreDetails(@PathVariable("id") Long storeId) {

        StoreResponseDto responseDto = storeService.getStoreDetails(storeId);

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
