    package gmart.gmart.service.image;

    import gmart.gmart.domain.UploadedImage;
    import gmart.gmart.domain.enums.UploadPurpose;
    import gmart.gmart.dto.image.ProfileImageUrlResponseDto;
    import gmart.gmart.exception.ErrorMessage;
    import gmart.gmart.exception.ImageCustomException;
    import gmart.gmart.repository.UploadedImageRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.multipart.MultipartFile;

    /**
     * 업로드 이미지 서비스
     */
    @Service
    @RequiredArgsConstructor
    @Transactional(readOnly = true)
    public class UploadImageService {


        private final UploadedImageRepository uploadImageRepository;
        private final LocalStorageService localStorageService;


        /**
         * ImageUrl로 업로드 이미지 조회
         * @param imageUrl
         * @return
         */
        public UploadedImage findByImageUrl(String imageUrl) {
            return uploadImageRepository.findByImageUrl(imageUrl)
                    .orElseThrow(() -> new ImageCustomException(ErrorMessage.NOT_FOUND_FILE));
        }

        /**
         * 프로필 이미지 업로드
         * @param file
         * @return
         */
        @Transactional
        public ProfileImageUrlResponseDto uploadProfileImage(MultipartFile file) {
            //실제 파일 저장
            String imageUrl = localStorageService.uploadFile(file);
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            //DB 저장
            UploadedImage uploadedImage = UploadedImage.createEntity(fileName, imageUrl, UploadPurpose.PROFILE);
            uploadImageRepository.save(uploadedImage);

            return ProfileImageUrlResponseDto.createDto(imageUrl);
        }
    }
