    package gmart.gmart.service.image;

    import gmart.gmart.domain.UploadedImage;
    import gmart.gmart.domain.enums.ImageDefaultStatus;
    import gmart.gmart.domain.enums.UploadPurpose;
    import gmart.gmart.dto.image.ImageUrlResponseDto;
    import gmart.gmart.exception.ErrorMessage;
    import gmart.gmart.exception.ImageCustomException;
    import gmart.gmart.repository.image.UploadedImageRepository;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.multipart.MultipartFile;

    /**
     * 업로드 회원 프로필 이미지 서비스
     */
    @Service
    @Transactional(readOnly = true)
    public class UploadMemberProfileImageService {


        private final UploadedImageRepository uploadImageRepository;

        private final StorageService storageService;

        public UploadMemberProfileImageService(
                UploadedImageRepository uploadImageRepository,
                @Qualifier("profileImageStorageService") StorageService storageService
        ) {
            this.uploadImageRepository = uploadImageRepository;
            this.storageService = storageService;
        }


        /**
         * ImageUrl로 업로드 이미지 조회
         * @param imageUrl
         * @return
         */
        public UploadedImage findByImageUrl(String imageUrl) {
            return uploadImageRepository.findByImageUrl(imageUrl)
                    .orElse(null);
        }

        /**
         * 프로필 이미지 업로드
         * @param file
         * @return
         */
        @Transactional
        public ImageUrlResponseDto uploadProfileImage(MultipartFile file) {
            //실제 파일 저장
            String imageUrl = storageService.uploadFile(file);
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            //DB 저장
            UploadedImage uploadedImage = UploadedImage.createEntity(fileName, imageUrl, UploadPurpose.PROFILE);
            uploadImageRepository.save(uploadedImage);

            return ImageUrlResponseDto.createDto(imageUrl);
        }

        /**
         * [서비스 로직]
         * 이미지 파일 삭제
         * @param imageUrl 이미지 URL
         */
        @Transactional
        public void deleteImageFile(String imageUrl) {
            storageService.deleteImageFile(imageUrl);
        }

        /**
         * [서비스 로직]
         * 회원 프로필 기본(디폴트) 이미지 조회
         * @return UploadedImage 업로드 이미지 엔티티
         */
        public UploadedImage findDefaultProfileImage() {
            return uploadImageRepository.findDefaultMemberProfileImage(ImageDefaultStatus.DEFAULT,UploadPurpose.PROFILE).orElseThrow(() -> new ImageCustomException(ErrorMessage.NOT_FOUND_FILE));
        }

        /**
         * [서비스 로직]
         * 업로드 취소
         * @param imageUrl 이미지 URL
         */
        @Transactional
        public void cancelUploadImage(String imageUrl) {
            UploadedImage uploadedImage = findByImageUrl(imageUrl);
            deleteImageFile(imageUrl);
            uploadImageRepository.delete(uploadedImage);
        }


    }
