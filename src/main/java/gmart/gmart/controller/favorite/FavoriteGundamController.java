package gmart.gmart.controller.favorite;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.favorite.FavoriteGundamListResponseDto;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;
import gmart.gmart.service.favorite.FavoriteGundamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 회원 관심 건담 컨트롤러
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/favorite-gundam")
public class FavoriteGundamController {

    private final FavoriteGundamService favoriteGundamService; //회원 관심 건담 서비스

    /**
     * [컨트롤러]
     * 회원 관심 건담 등록
     * @param id 건담 아이디
     * @return 성공 메시지
     */
    @PostMapping("/gundam/{id}")
    public ResponseEntity<ApiResponse<?>> createFavoriteGundam(@PathVariable("id") Long id) {

        favoriteGundamService.createFavoriteGundam(id);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","회원 관심 건담 등록 성공")));
    }

    /**
     * [컨트롤러]
     * 회원 관심 건담 삭제
     * @param id 관심 건담 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteFavoriteGundam(@PathVariable("id") Long id) {
        favoriteGundamService.deleteFavoriteGundam(id);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","회원 관심 건담 삭제 성공")));
    }


    /**
     * [컨트롤러]
     * 조건에 따른 회원 관심 건담 리스트 조회(로그인한 회원)
     * @param condDto 검색 조건 DTO
     * @return List<FavoriteGundamListResponseDto> 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllFavoriteGundam(@RequestBody SearchFavoriteGundamCondDto condDto) {

        List<FavoriteGundamListResponseDto> responseDtos = favoriteGundamService.findAllByCond(condDto);

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
