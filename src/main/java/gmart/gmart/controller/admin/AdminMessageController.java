package gmart.gmart.controller.admin;

import gmart.gmart.domain.enums.AdminMessageType;
import gmart.gmart.dto.adminmessage.AdminMessageListResponseDto;
import gmart.gmart.dto.adminmessage.CreateAdminMessageRequestDto;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.admin.AdminMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 관리자 메시지 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/admin-message")
public class AdminMessageController {

    private final AdminMessageService adminMessageService; //관리자 메시지 서비스

    /**
     * [컨트롤러]
     * 관리자 - 특정 회원에게 관리자 메시지를 보내기 (등록)
     * @param memberId 회원 아이디
     * @param requestDto 등록 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/member/{id}")
    public ResponseEntity<ApiResponse<?>> createAdminMessage(@PathVariable("id") Long memberId, @Valid @RequestBody CreateAdminMessageRequestDto requestDto, BindingResult bindingResult) {
        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        adminMessageService.createMessage(memberId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관리자 메시지 등록 완료")));
    }

    /**
     * [컨트롤러]
     * 관리자 - 모든 회원에게 관리자 메시지를 보내기 (등록)
     * @param requestDto 등록 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("/member")
    public ResponseEntity<ApiResponse<?>> createAdminMessageForAllMember(@Valid @RequestBody CreateAdminMessageRequestDto requestDto, BindingResult bindingResult) {
        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        adminMessageService.createMessageForAllMembers(requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관리자 메시지 등록 완료")));

    }

    /**
     * [컨트롤러]
     * 관리자 - 관리자 메시지 논리적 삭제 (SOFT DELETE)
     * @param adminMessageId 관리자 메시지 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> softDeleteAdminMessage(@PathVariable("id") Long adminMessageId) {

        adminMessageService.softDelete(adminMessageId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관리자 메시지 삭제 완료")));

    }


    /**
     * [컨트롤러]
     * 관리자 - 특정 회원이 받은 관리자 메시지 목록 조회
     * @param memberId 회원 아이디
     * @param content 메시지 내용
     * @param adminMessageType 메시지 타입
     * @param createdDateSortType 날짜 정렬 타입
     * @return PagedResponseDto<AdminMessageListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("/member/{id}")
    public ResponseEntity<ApiResponse<?>> getAdminMessages(@PathVariable("id")Long memberId,
                                                                 @RequestParam(value = "content" , required = false)String content,
                                                                 @RequestParam(value = "adminMessageType",required = false) AdminMessageType adminMessageType,
                                                                 @RequestParam(value = "createdDateSortType") CreatedDateSortType createdDateSortType
    ) {
        SearchAdminMessageCondDto condDto = SearchAdminMessageCondDto.create(content, adminMessageType, createdDateSortType);

        PagedResponseDto<AdminMessageListResponseDto> responseDtos = adminMessageService.findAllByCond(memberId, condDto);

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
