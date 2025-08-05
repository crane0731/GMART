package gmart.gmart.controller.admin;

import gmart.gmart.domain.enums.ChargeType;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gpoint.GPointChargeLogListResponseDto;
import gmart.gmart.dto.gpoint.GPointChargeRequestDto;
import gmart.gmart.dto.gpoint.GPointRefundRequestDto;
import gmart.gmart.dto.gpoint.SearchGPointChargeLogCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.admin.AdminGPointChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 관리자 건포인트 충전 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/gpoint-charge")
public class AdminGPointChargeController {

    private final AdminGPointChargeService adminGPointChargeService;//관리자 건포인트 충전 서비스

    /**
     * [컨트롤러]
     * 관리자- 모든 회원의 건포인트 충전 + 로그 저장
     * @param requestDto 충전 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/charge/all")
    public ResponseEntity<ApiResponse<?>> AllMembersChargeGPoint(@RequestBody GPointChargeRequestDto requestDto) {

        adminGPointChargeService.allMembersChargeGPoint(requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message","모든 회원 건포인트 충전 성공")));
    }

    /**
     * [컨트롤러]
     * 관리자 - 모든 회원의 건포인트 회수 + 로그 저장
     * @param requestDto 회수 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/refund/all")
    public ResponseEntity<ApiResponse<?>> AllMembersRefundGPoint(@RequestBody GPointRefundRequestDto requestDto) {

        adminGPointChargeService.allMembersRefundGPoint(requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message","모든 회원 건포인트 회수 성공")));
    }

    /**
     * [컨트롤러]
     * 관리자 - 특정 회원의 건포인트 충전 + 로그 저장
     * @param memberId 회원 아이디
     * @param requestDto 충전 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/charge/member/{id}")
    public ResponseEntity<ApiResponse<?>> chargeGPoint(@PathVariable("id") Long memberId, @RequestBody GPointChargeRequestDto requestDto) {

        adminGPointChargeService.chargeGPoint(memberId,requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message","건포인트 충전 성공")));
    }


    /**
     * [컨트롤러]
     * 관리자 - 특정 회원의 건포인트 회수 + 로그 저장
     * @param memberId 회원 아이디
     * @param requestDto 회수 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/refund/member/{id}")
    public ResponseEntity<ApiResponse<?>> refundGPoint(@PathVariable("id") Long memberId,@RequestBody GPointRefundRequestDto requestDto) {

        adminGPointChargeService.refundGPoint(memberId,requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message","건포인트 회수 성공")));
    }

    /**
     * [컨트롤러]
     * 관리자 - 특정 회원의 건포인트 충전 로그 리스트 조회(검색 조건에 따라)
     * @param memberId 회원 아이디
     * @param year 연도
     * @param chargeType 충전 타입
     * @param page 페이지 번호
     * @return PagedResponseDto<GPointChargeLogListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/member/{id}")
    public ResponseEntity<ApiResponse<?>> findAllLogs(@PathVariable("id") Long memberId,
                                                      @RequestParam(value = "year",required = false)String year,
                                                      @RequestParam(value = "chargeType",required = false) ChargeType chargeType,
                                                      @RequestParam(value = "page",defaultValue = "0")int page) {

        SearchGPointChargeLogCondDto condDto = SearchGPointChargeLogCondDto.create(year, chargeType);

        PagedResponseDto<GPointChargeLogListResponseDto> responseDtos = adminGPointChargeService.findAllByCond(memberId, condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));

    }
}
