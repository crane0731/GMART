package gmart.gmart.controller.member;

import gmart.gmart.domain.enums.AdminMessageType;
import gmart.gmart.dto.adminmessage.AdminMessageListResponseDto;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.admin.AdminMessageService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 - 관리자 메시지 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin-message")
public class MemberAdminMessageController {

    private final AdminMessageService adminMessageService;//관리자 메시지 서비스

    /**
     * [컨트롤러]
     * 회원이 자신의 관리자 메시지 목록을 조회
     * @param content 메시지 내용
     * @param adminMessageType 메시지 타입
     * @param createdDateSortType 날짜 정렬 타입
     * @return PagedResponseDto<AdminMessageListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getMemberAdminMessages(@RequestParam(value = "content" , required = false)String content,
                                                                 @RequestParam(value = "adminMessageType",required = false) AdminMessageType adminMessageType,
                                                                 @RequestParam(value = "createdDateSortType") CreatedDateSortType createdDateSortType
    ) {
        SearchAdminMessageCondDto condDto = SearchAdminMessageCondDto.create(content, adminMessageType, createdDateSortType);

        PagedResponseDto<AdminMessageListResponseDto> responseDtos = adminMessageService.findMyAdminMessage(condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
    }
}
