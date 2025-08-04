package gmart.gmart.controller.report;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.report.CreateReportRequestDto;
import gmart.gmart.service.report.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/report")
public class    ReportController {

    private final ReportService reportItemService; //신고 상품 서비스


    /**
     * [컨트롤러]
     * 상품 신고
     * 구매자 - > 판매자
     * 판매자 -> 구매자
     * 양쪽 신고 모두 가능
     * @param requestDto 신고 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> reportItem(@Valid @RequestBody CreateReportRequestDto requestDto, BindingResult bindingResult){
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        reportItemService.report(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상품 신고 완료")));

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
