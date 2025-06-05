package gmart.gmart.controller.admin;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gmoney.GMoneyChargeLogListResponseDto;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
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
     * @param condDto 검색 조건 DTO
     * @return List<GMoneyChargeLogListResponseDto> 응답 DTO 리스트
     */
    @PostMapping("/member/{id}")
    public ResponseEntity<ApiResponse<?>> findAllLogs(@PathVariable("id") Long memberId, @RequestBody SearchGMoneyChargeLogCondDto condDto) {

        List<GMoneyChargeLogListResponseDto> responseDtos = adminGMoneyChargeService.findAllLogs(memberId, condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));

    }

}
