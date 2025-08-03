package gmart.gmart.controller.gpoint;

import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.enums.GPointDeltaType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gmoney.GMoneyLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import gmart.gmart.dto.gpoint.GPointLogListResponseDto;
import gmart.gmart.dto.gpoint.SearchGPointLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.gpoint.GPointLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 건포인트 거래 로그 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/gpoint")
public class GPointLogController {

    private final GPointLogService gPointLogService; //건포인트 로그 서비스

    /**
     * [컨트롤러]
     * 현재 로그인한 회원의 건포인트 거래 로그 목록 조회
     * @param year 년도
     * @param gPointDeltaType 건포인트 변화 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GMoneyChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getAllGMoneyLogs(@RequestParam(value = "year",required = false)String year,
                                                           @RequestParam(value = "gPointDeltaType",required = false) GPointDeltaType gPointDeltaType,
                                                           @RequestParam(value = "page",defaultValue = "0")int page) {


        SearchGPointLogCondDto condDto = SearchGPointLogCondDto.create(year, gPointDeltaType);

        PagedResponseDto<GPointLogListResponseDto> responseDtos = gPointLogService.findAllLogs(condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
    }


}
