package gmart.gmart.controller;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.article.ArticleResponseDto;
import gmart.gmart.dto.article.CreateArticleRequestDto;
import gmart.gmart.dto.article.UpdateArticleRequestDto;
import gmart.gmart.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 게시글 컨트롤러
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/article")
public class ArticleController {

    private final ArticleService articleService; //게시글 서비스

    /**
     * [컨트롤러]
     * 게시글 등록
     * @param requestDto 게시글 등록 요청 DTO
     * @param bindingResult 에러 메시지를 바인딩 할 객체
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createArticle(@Valid @RequestBody CreateArticleRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //게시글 등록
        articleService.createArticle(requestDto);


        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 등록 완료")));

    }

    /**
     * [컨트롤러]
     * 게시글 삭제
     * @param articleId 게시글 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> createArticle(@PathVariable("id")Long articleId) {

        articleService.deleteArticle(articleId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 삭제 완료")));

    }

    /**
     * [컨트롤러]
     * 게시글 좋아요
     * @param articleId 게시글 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> likeArticle(@PathVariable("id")Long articleId) {

        articleService.likeArticle(articleId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 좋아요 완료")));
    }

    /**
     * [컨트롤러]
     * 게시글 좋아요 취소
     * @param articleId 게시글 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> unlikeArticle(@PathVariable("id")Long articleId) {

        articleService.unlikeArticle(articleId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 좋아요 취소 완료")));
    }

    /**
     * [컨트롤러]
     * 게시글 수정
     * @param articleId 게시글 아이디
     * @param requestDto 수정 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateArticle(@PathVariable("id")Long articleId, @Valid @RequestBody UpdateArticleRequestDto requestDto, BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        articleService.updateArticle(articleId,requestDto);


        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","게시글 수정 완료")));
    }


    /**
     * [컨트롤러]
     * 게시글 단일 조회(상세)
     * @param articleId 게시글 아이디
     * @return  ArticleResponseDto 응답 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getArticle(@PathVariable("id")Long articleId) {

        ArticleResponseDto responseDto = articleService.getArticleInfo(articleId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }


    /**
     *게시글 리스트 조회
     */


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
