package gmart.gmart.service.image;


import gmart.gmart.domain.UploadedImage;
import gmart.gmart.domain.enums.ImageDefaultStatus;
import gmart.gmart.domain.enums.UploadPurpose;
import gmart.gmart.dto.image.ImageUrlResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ImageCustomException;
import gmart.gmart.repository.UploadedImageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 업로드 상점 프로필 이미지 서비스
 */
@Service
@Transactional(readOnly = true)
public class UploadStoreProfileImageService {

    private final UploadedImageRepository uploadImageRepository;

    private final StorageService storageService;

    public UploadStoreProfileImageService(
            UploadedImageRepository uploadImageRepository,
            @Qualifier("storeImageStorageService") StorageService storageService
    ) {
        this.uploadImageRepository = uploadImageRepository;
        this.storageService = storageService;
    }



    /**
     * 상점 프로필 이미지 업로드
     * @param file 멀티파트 파일
     * @return
     */
    @Transactional
    public ImageUrlResponseDto uploadProfileImage(MultipartFile file) {
        //실제 파일 저장
        String imageUrl = storageService.uploadFile(file);
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        //DB 저장
        UploadedImage uploadedImage = UploadedImage.createEntity(fileName, imageUrl, UploadPurpose.STORE);
        uploadImageRepository.save(uploadedImage);

        return ImageUrlResponseDto.createDto(imageUrl);
    }

    /**
     * [서비스 로직]
     * 업로드 이미지 삭제
     * @param imageUrl 이미지 URL
     */
    @Transactional
    public void deleteImageFile(String imageUrl) {
        storageService.deleteImageFile(imageUrl);
    }

    /**
     * [조회]
     * ImageUrl로 업로드 이미지 조회
     * @param imageUrl 이미지 URL
     * @return  UploadedImage 업로드 이미지 엔티티
     */
    public UploadedImage findByImageUrl(String imageUrl) {
        return uploadImageRepository.findByImageUrl(imageUrl)
                .orElse(null);
    }

    /**
     * [조회]
     * 회원 프로필 기본(디폴트) 이미지 조회
     * @return UploadedImage 업로드 이미지 엔티티
     */
    public UploadedImage findDefaultProfileImage() {
        return uploadImageRepository.findDefaultMemberProfileImage(ImageDefaultStatus.DEFAULT, UploadPurpose.STORE).orElseThrow(() -> new ImageCustomException(ErrorMessage.NOT_FOUND_FILE));
    }


}
