package gmart.gmart.controller.favorite;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.favorite.FavoriteStoreListResponseDto;
import gmart.gmart.dto.favorite.SearchFavoriteStoreCondDto;
import gmart.gmart.service.favorite.FavoriteStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/favorite-store")
public class FavoriteStoreController {

    private final FavoriteStoreService favoriteStoreService;//관심 상점 서비스

    /**
     * [컨트롤러]
     * 회원 관심 상점 등록
     * @param storeId 상점 아이디
     * @return 성공메시지
     */
    @PostMapping("/store/{id}")
    public ResponseEntity<ApiResponse<?>> addFavoriteStore(@PathVariable("id")Long storeId){

        favoriteStoreService.addFavoriteStore(storeId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관심 상점 등록 성공")));
    }

    /**
     * [컨트롤러]
     * 회원 관심 상점 논리 삭제 (SOFT DELETE)
     * @param storeId 상점 아이디
     * @return 성공메시지
     */
    @DeleteMapping("/store/{id}")
    public ResponseEntity<ApiResponse<?>> deleteFavoriteStore(@PathVariable("id")Long storeId){

        favoriteStoreService.deleteFavoriteStore(storeId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관심 상점 삭제 성공")));
    }

    /**
     * [컨트롤러]
     * 로그인한 회원의 관심 상점 리스트 조회
     * 상점의 이름으로도 검색 가능
     * @param name 상점 이름
     * @return List<FavoriteStoreListResponseDto> 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getFavoriteStore(@RequestParam("name") String name){

        SearchFavoriteStoreCondDto condDto = SearchFavoriteStoreCondDto.create(name);

        List<FavoriteStoreListResponseDto> responseDtos = favoriteStoreService.getFavoriteStoreList(condDto);

        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }

}
