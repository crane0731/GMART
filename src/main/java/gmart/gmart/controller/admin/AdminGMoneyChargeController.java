package gmart.gmart.controller.admin;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.admin.AdminGMoneyChargeService;
import gmart.gmart.service.gmoney.GMoneyChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 건머니 충전 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/gmoney-charge")
public class AdminGMoneyChargeController {

    private final AdminGMoneyChargeService adminGMoneyChargeService;//관리자 건머니 충전 서비스



    /**
     * [컨트롤러]
     * 관리자 - 특정 회원의 건머니 충전 로그 목록 조회
     * @param memberId 회원 아이디
     * @param year 년도
     * @param chargeType 충전 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GMoneyChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/member/{id}")
    public ResponseEntity<ApiResponse<?>> findAllLogs(@PathVariable("id") Long memberId,
                                                      @RequestParam(value = "year",required = false)String year,
                                                      @RequestParam(value = "chargeType",required = false) ChargeType chargeType,
                                                      @RequestParam(value = "page",defaultValue = "0")int page) {

        SearchGMoneyChargeLogCondDto condDto = SearchGMoneyChargeLogCondDto.create(year, chargeType);

        PagedResponseDto<GMoneyChargeLogListResponseDto> responseDtos = adminGMoneyChargeService.findAllLogs(memberId, condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));

    }

}
