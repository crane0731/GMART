package gmart.gmart.controller.gmoney;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.GMoneyChargeRequestDto;
import gmart.gmart.dto.gmoney.GMoneyRefundRequestDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.gmoney.GMoneyChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 건머니 충전 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/gmoney-charge")
public class    GMoneyChargeController {

    private final GMoneyChargeService gMoneyChargeService; //건머니 충전 서비스


    /**
     * [컨트롤러]
     * 건머니 충전 + 충전 로그 생성
     * @param requestDto 건머니 충전 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/charge")
    public ResponseEntity<ApiResponse<?>> chargeGMoney(@Valid @RequestBody GMoneyChargeRequestDto requestDto,BindingResult bindingResult) {


        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        gMoneyChargeService.chargeGMoney(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","건머니 충전 성공")));
    }

    /**
     * [컨트롤러]
     * 건머니 환불 + 환불 로그 생성
     * @param requestDto 건머니 충전 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<?>> refundGMoney(@Valid @RequestBody GMoneyRefundRequestDto requestDto,BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        gMoneyChargeService.refundGMoney(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","건머니 환불 성공")));
    }



    /**
     * [컨트롤러]
     * 현재 로그인한 회원의 건머니 충전 로그 목록 조회
     * @param year 년도
     * @param chargeType 충전 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GMoneyChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getAllGMoneyCharges(@RequestParam(value = "year",required = false)String year,
                                                              @RequestParam(value = "chargeType",required = false) ChargeType chargeType,
                                                              @RequestParam(value = "page",defaultValue = "0")int page) {


        SearchGMoneyChargeLogCondDto condDto = SearchGMoneyChargeLogCondDto.create(year, chargeType);

        PagedResponseDto<GMoneyChargeLogListResponseDto> responseDtos = gMoneyChargeService.findAllLogs(condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
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
