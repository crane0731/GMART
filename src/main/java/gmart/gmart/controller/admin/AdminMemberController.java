package gmart.gmart.controller.admin;


import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.dto.member.MemberSuspensionRequestDto;
import gmart.gmart.dto.mybatis.MemberListResponseDto;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import gmart.gmart.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * 관리자가 회원 삭제
     * @param id 회원 아이디
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteMember(@PathVariable("id") Long id){

        //회원 삭제
        adminMemberService.deleteMember(id);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "회원 삭제 성공")));

    }


    /**
     * 관리자가 회원 계정 일시 정지 (계정 잠금)
     * @param id 회원아이디
     * @param requestDto 회원정지요청 DTO
     * @return
     */
    @PostMapping("/{id}/suspension")
    public ResponseEntity<ApiResponse<?>> suspensionMember(@PathVariable("id") Long id, @RequestBody MemberSuspensionRequestDto requestDto){

        //회원 계정 일시 정지
        adminMemberService.suspensionMember(id, requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "회원 일시 정지 성공")));

    }

    /**
     * 관리자가 회원 활동 정지 해제
     * @param id 회원 아이디
     * @return
     */
    @PostMapping("/{id}/release")
    public ResponseEntity<ApiResponse<?>> releaseMember(@PathVariable("id") Long id){

        //회원 계정 일시 정지 해제
        adminMemberService.releaseMember(id);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "회원 일시 정지 해제 성공")));

    }

    /**
     * 관리자가 회원 전체를 조회하는 컨트롤러
     * @param requestDto 필터링 조회 요청 DTO
     * @return 회원 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> findAll(@RequestBody SearchMemberListDto requestDto){

        List<MemberListResponseDto> responseDtos = adminMemberService.findAll(requestDto);

        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }






}
