package gmart.gmart.controller.image;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.image.ImageUrlResponseDto;
import gmart.gmart.service.image.UploadItemImageService;
import gmart.gmart.service.image.UploadMemberProfileImageService;
import gmart.gmart.service.image.UploadStoreProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 이미지 관련 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/image")
public class ImageController {

    private final UploadMemberProfileImageService profileImageService; //회원 프로필 이미지 서비스
    private final UploadStoreProfileImageService storeProfileImageService;//상점 프로필 이미지 서비스
    private final UploadItemImageService itemImageService;//상품 이미지 서비스

    /**
     * [컨트롤러]
     * 회원 프로필 이미지 업로드
     * @param file 멀티파트 파일
     * @return  ImageUrlResponseDto 이미지 URL 응답 DTO
     */
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<?>> profileImageUpload(@RequestParam("file") MultipartFile file) {

        ImageUrlResponseDto responseDto = profileImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * [컨트롤러]
     * 회원 프로필 이미지 업로드 취소
     * @param imageUrl 업로드 취소할 이미지 URL
     * @return 성공 메시지
     */
    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<?>> cancelProfileImageUpload(@RequestParam("imageUrl") String imageUrl) {

        profileImageService.cancelUploadImage(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }


    /**
     * [컨트롤러]
     * 상점 프로필 이미지 업로드
     * @param file 멀티파트 파일
     * @return  ImageUrlResponseDto 이미지 URL 응답 DTO
     */
    @PostMapping("/store")
    public ResponseEntity<ApiResponse<?>> storeImageUpload(@RequestParam("file") MultipartFile file) {

        ImageUrlResponseDto responseDto = storeProfileImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * [컨트롤러]
     * 상점 프로필 이미지 업로드 취소
     * @param imageUrl 업로드 취소할 이미지 URL
     * @return 성공 메시지
     */
    @DeleteMapping("/store")
    public ResponseEntity<ApiResponse<?>> cancelStoreImageUpload(@RequestParam("imageUrl") String imageUrl) {

        storeProfileImageService.cancelUploadImage(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }

    /**
     * [컨트롤러]
     * 상점 프로필 이미지 업로드
     * @param file 멀티파트 파일
     * @return  ImageUrlResponseDto 이미지 URL 응답 DTO
     */
    @PostMapping("/item")
    public ResponseEntity<ApiResponse<?>> itemImageUpload(@RequestParam("file") MultipartFile file) {

        ImageUrlResponseDto responseDto = itemImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * [컨트롤러]
     * 상점 프로필 이미지 업로드 취소
     * @param imageUrl 업로드 취소할 이미지 URL
     * @return 성공 메시지
     */
    @DeleteMapping("/item")
    public ResponseEntity<ApiResponse<?>> cancelItemImageUpload(@RequestParam("imageUrl") String imageUrl) {

        itemImageService.cancelUploadImage(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }

    }
