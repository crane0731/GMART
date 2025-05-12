package gmart.gmart.controller.admin;


import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자용 회원 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    /**
     * 관리자가 회원 상세 조회
     * @param id 회원 아이디(PK)
     * @return MemberInfoResponseDto 회원 상세 정보를 담은 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> findMemberDetailInfo(@PathVariable("id") Long id){

        //회원 정보 조회
        MemberInfoResponseDto responseDto = adminMemberService.findMemberDetailInfo(id);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * 관리자가 회원 전체 조회
     */

    /**
     * 관리자가 회원 삭제
     */

    /**
     * 관리자가 회원 활동 일시 정지 시키기
     */





}
