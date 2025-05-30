package gmart.gmart.controller.image;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.image.ProfileImageUrlResponseDto;
import gmart.gmart.service.image.UploadArticleImageService;
import gmart.gmart.service.image.UploadMemberProfileImageService;
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
    private final UploadArticleImageService articleImageService; //게시글 이미지 서비스

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<?>> profileImageUpload(@RequestParam("file") MultipartFile file) {

        ProfileImageUrlResponseDto responseDto = profileImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * 게시글 이미지 업로드
     */
    @PostMapping("/article")
    public ResponseEntity<ApiResponse<?>> articleImageUpload(@RequestParam("file") MultipartFile file) {

        ProfileImageUrlResponseDto responseDto = articleImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * 프로필 이미지 업로드 취소
     */
    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<?>> cancelProfileImageUpload(@RequestParam("imageUrl") String imageUrl) {

        profileImageService.deleteProfileImage(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }

    /**
     * 게시글 이미지 업로드 취소
     */
    @DeleteMapping("/article")
    public ResponseEntity<ApiResponse<?>> cancelArticleImageUpload(@RequestParam("imageUrl") String imageUrl) {

        articleImageService.deleteArticleImage(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }

    }
