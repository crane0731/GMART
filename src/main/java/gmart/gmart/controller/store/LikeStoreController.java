package gmart.gmart.controller.store;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.service.store.LikeStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 상점 좋아요 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/store")
public class LikeStoreController {

    private final LikeStoreService likeStoreService; //상점 좋아요 서비스

    /**
     * [컨트롤러]
     * 상점 좋아요
     * @param storeId 상점 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> likeStore(@PathVariable("id") Long storeId){

        likeStoreService.likeStore(storeId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상점 좋아요 성공")));
    }

    /**
     * [컨트롤러]
     * 상점 좋아요 취소
     * @param storeId 상점 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> cancellikeStore(@PathVariable("id") Long storeId){

        likeStoreService.cancelLikeStore(storeId);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","상점 좋아요 취소 성공")));
    }


}
