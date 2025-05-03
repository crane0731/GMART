package gmart.gmart.controller.image;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.image.ProfileImageUrlResponseDto;
import gmart.gmart.service.image.ProfileImageService;
import gmart.gmart.service.image.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 이미지 관련 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart")
public class ImageController {

    private final ProfileImageService profileImageService;
    private final StorageService storageService;

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/profile/image")
    public ResponseEntity<ApiResponse<?>> profileImageUpload(@RequestParam("file") MultipartFile file) {

        ProfileImageUrlResponseDto responseDto = profileImageService.uploadProfileImage(file);

        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * 프로필 이미지 업로드 취소
     */
    @DeleteMapping("/profile/image")
    public ResponseEntity<ApiResponse<?>> cancelProfileImageUpload(@RequestParam("imageUrl") String imageUrl) {

        storageService.deleteImageFile(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "이미지 업로드 취소 성공")));

    }

    }
