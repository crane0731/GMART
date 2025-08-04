package gmart.gmart.controller.admin;


import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.enums.MemberSortType;
import gmart.gmart.dto.enums.SortDirection;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.dto.member.MemberSuspensionRequestDto;
import gmart.gmart.dto.mybatis.MemberListResponseDto;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.suspension.MemberSuspensionListResponseDto;
import gmart.gmart.service.admin.AdminMemberService;
import gmart.gmart.service.member.MemberSuspensionService;
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

    private final AdminMemberService adminMemberService; //관리자 회원 서비스
    private final MemberSuspensionService memberSuspensionService;  //회원 계정 정지 서비스

    /**
     * [컨트롤러]
     * 관리자가 특정 회원의 계정 정지 내역 목록을 조회
     * @param id 회원 아이디
     * @return List<MemberSuspensionListResponseDto>
     */
    @GetMapping("/{id}/suspension")
    public ResponseEntity<ApiResponse<?>> findMemberSuspension(@PathVariable("id") Long id) {

        return ResponseEntity.ok(ApiResponse.success(memberSuspensionService.findAll(id)));

    }


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
     * [컨트롤러]
     * 관리자가 검색 조건에 따라 회원 전체를 조회하는 컨트롤러
     * @param nickname 닉네임
     * @param loginId 로그인 아이디
     * @param sortType 정렬 타입
     * @param sortDirection 정렬 방향
     * @param page 페이지 번호
     * @return PagedResponseDto<MemberListResponseDto>
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> findAll(@RequestParam (value = "nickname" , required = false) String nickname,
                                                  @RequestParam (value = "loginId",required = false)String loginId,
                                                  @RequestParam(value = "sortType", required = false) MemberSortType sortType,
                                                  @RequestParam(value = "sortDirection",required = false) SortDirection sortDirection,
                                                  @RequestParam(value = "page",defaultValue = "0") int page
                                                  ){

        return ResponseEntity.ok(ApiResponse.success(adminMemberService.findAll(SearchMemberListDto.create(nickname, loginId, sortType, sortDirection), page)));
    }






}
