package gmart.gmart.service.image;

import gmart.gmart.domain.UploadedImage;
import gmart.gmart.domain.enums.UploadPurpose;
import gmart.gmart.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 정해진 시간마다 사용되지 않은 업로드 이미지를 삭제하는 스케쥴 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadedImageCleanupService {

    private final UploadMemberProfileImageService uploadMemberProfileImageService; //회원 프로필 이미지 서비스
    private final UploadArticleImageService uploadArticleImageService; //게시글 이미지 서비스
    private final UploadItemImageService uploadItemImageService;//상품 이미지 서비스
    private final UploadStoreProfileImageService uploadStoreProfileImageService;//상점 이미지 서비스

    private final UploadedImageRepository uploadImageRepository; //업로드 이미지 레파지토리

    /**
     * 매일 새벽 3시에 실행 (초 분 시 일 월 요일)
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteUnusedImages() {

        log.info("스케쥴러 시작됨");
        List<UploadedImage> unusedImages = uploadImageRepository.findByIsUsedFalse();

        for (UploadedImage image : unusedImages) {

            try{
                if(image.getPurpose()== UploadPurpose.PROFILE) {
                    //실제 파일 삭제
                    uploadMemberProfileImageService.deleteImageFile(image.getImageUrl());
                }
                else if(image.getPurpose()== UploadPurpose.ARTICLE){
                    //실제 파일 삭제
                    uploadArticleImageService.deleteImageFile(image.getImageUrl());

                }
                else if(image.getPurpose()== UploadPurpose.ITEM){
                    //실제 파일 삭제
                    uploadItemImageService.deleteImageFile(image.getImageUrl());
                }
                else if(image.getPurpose()== UploadPurpose.STORE){
                    //실제 파일 삭제
                    uploadStoreProfileImageService.deleteImageFile(image.getImageUrl());
                }

                //DB에서 삭제
                uploadImageRepository.delete(image);


            }catch (Exception e) {
                log.info("파일삭제 실패 = {}",image.getImageUrl());
            }
        }
    }

}
