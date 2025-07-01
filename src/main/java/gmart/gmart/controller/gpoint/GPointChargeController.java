package gmart.gmart.controller.gpoint;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gpoint.GPointChargeLogListResponseDto;
import gmart.gmart.dto.gpoint.SearchGPointChargeLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.gpoint.GPointChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 건포인트 충전 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/gpoint-charge")
public class GPointChargeController {

    private final GPointChargeService gPointChargeService;//건포인트 충전 서비스

    /**
     * [컨트롤러]
     * 회원 자신의 건포인트 충전 로그 리스트 조회(검색 조건에 따라)
     * @param year 년도
     * @param chargeType 충전 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GPointChargeLogListResponseDto> 패이징된 응답 DTO 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> findAllLogs(@RequestParam(value = "year",required = false)String year,
                                                      @RequestParam(value = "chargeType",required = false) ChargeType chargeType,
                                                      @RequestParam(value = "page",defaultValue = "0")int page){

        SearchGPointChargeLogCondDto condDto = SearchGPointChargeLogCondDto.create(year, chargeType);

        PagedResponseDto<GPointChargeLogListResponseDto> responseDtos = gPointChargeService.findAllLogs(condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));

    }

}
