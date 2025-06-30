package gmart.gmart.controller.favorite;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.favorite.FavoriteItemListResponseDto;
import gmart.gmart.service.favorite.FavoriteItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 회원 관심 상품 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/favorite-item")
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;//관심 상품 서비스


    /**
     * [컨트롤러]
     * 관심 상품 등록
     * @param itemId 상품 아이디
     * @return 성공 메시지
     */
    @PostMapping("item/{id}")
    public ResponseEntity<ApiResponse<?>> createFavoriteItem(@PathVariable("id") Long itemId){

        favoriteItemService.createFavoriteItem(itemId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관심 상품 등록 성공")));

    }

    /**
     * [컨트롤러]
     * 관삼 상품 존재 상태 반환
     * true : 존재
     * false : 없음
     * @param itemId 상품 아이디
     * @return boolean
     */
    @GetMapping("/item/{id}")
    public ResponseEntity<ApiResponse<?>> getFavoriteItem(@PathVariable("id") Long itemId){

        boolean favoriteItemStatus = favoriteItemService.getFavoriteItemStatus(itemId);

        return ResponseEntity.ok().body(ApiResponse.success(favoriteItemStatus));

    }

    /**
     * [컨트롤러]
     * 관심 상품 논리적 삭제 (SOFT DELETE)
     * @param itemId 관심 상품 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("item/{id}")
    public ResponseEntity<ApiResponse<?>> deleteFavoriteItem(@PathVariable("id") Long itemId){

        favoriteItemService.deleteFavoriteItem(itemId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","관심 상품 삭제 성공")));

    }

    /**
     * [컨트롤러]
     * 검색 조건에 따라 현재 로그인한 회원의 관심 상품 목록 조회
     * @param itemTitle 상품제목
     * @return List<FavoriteItemListResponseDto> 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getFavoriteItems(@RequestParam(value = "itemTitle",required = false)String itemTitle){

        List<FavoriteItemListResponseDto> responseDtos = favoriteItemService.getAllFavoriteItems(itemTitle);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));

    }


}
