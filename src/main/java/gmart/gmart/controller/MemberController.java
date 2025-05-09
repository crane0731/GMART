package gmart.gmart.controller;

import gmart.gmart.dto.MemberGundamGradeListDto;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.dto.member.UpdateMemberInfoRequestDto;
import gmart.gmart.dto.password.ChangePasswordRequestDto;
import gmart.gmart.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 나의 회원 정보 컨트롤러
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> findMyInfo(){
        MemberInfoResponseDto myMemberInfo = memberService.getMyMemberInfo();

        return ResponseEntity.ok(ApiResponse.success(myMemberInfo));

    }

    /**
     * 선호하는 건담 등급 등록 or 변경 컨트롤러
     * @param requestDto
     * @return
     */
    @PostMapping("/gundamgrade")
    public ResponseEntity<ApiResponse<?>> addGundamGrade(@RequestBody MemberGundamGradeListDto requestDto){

        memberService.updateGundamGrade(requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message","선호하는 건담 등급 등록 성공")));
    }


    /**
     * 회원 비밀번호 변경 컨트롤러
     * @param requestDto
     * @param bindingResult
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto, BindingResult bindingResult){

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //비밀번호 업데이트
        memberService.updatePassword(requestDto);

        return  ResponseEntity.ok(ApiResponse.success(Map.of("message","비밀번호 변경 성공")));


    }

    //회원 자신의 정보 수정
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> UpdateMyInfo(@RequestBody UpdateMemberInfoRequestDto requestDto , BindingResult bindingResult){

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();


        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        memberService.updateMemberInfo(requestDto);

        return  ResponseEntity.ok(ApiResponse.success(Map.of("message","수정 완료")));
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
