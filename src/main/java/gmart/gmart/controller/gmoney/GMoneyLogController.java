package gmart.gmart.controller.gmoney;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.GMoneyLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.gmoney.GMoneyLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 건머니 거래 로그 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/gmoney")
public class GMoneyLogController {


    private final GMoneyLogService gMoneyLogService; //건머니 로그 서비스


    /**
     * [컨트롤러]
     * 현재 로그인한 회원의 건머니 거래 로그 목록 조회
     * @param year 년도
     * @param gMoneyDeltaType 건머니 변화 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GMoneyChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getAllGMoneyLogs(@RequestParam(value = "year",required = false)String year,
                                                              @RequestParam(value = "gMoneyDeltaType",required = false) GMoneyDeltaType gMoneyDeltaType,
                                                              @RequestParam(value = "page",defaultValue = "0")int page) {


        SearchGMoneyLogCondDto condDto = SearchGMoneyLogCondDto.create(year, gMoneyDeltaType);

        PagedResponseDto<GMoneyLogListResponseDto> responseDtos = gMoneyLogService.findAllLogs(condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
    }

}
