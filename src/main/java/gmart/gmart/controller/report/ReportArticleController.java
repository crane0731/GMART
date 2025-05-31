package gmart.gmart.controller.report;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.reportarticle.CreateReportArticleRequestDto;
import gmart.gmart.service.report.ReportArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 게시글 신고 컨트롤러
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/report/article")
public class ReportArticleController {


    private final ReportArticleService reportArticleService; //게시글 신고 서비스

    /**
     * [컨트롤러]
     * 게시글 신고
     * @param articleId 게시글 아이디
     * @param requestDto 게시글 신고 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> reportArticle(@PathVariable("id") Long articleId, @Valid @RequestBody CreateReportArticleRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //게시글 신고
        reportArticleService.report(articleId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 신고 완료")));

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
