package gmart.gmart.service.image;

import gmart.gmart.domain.UploadedImage;
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

    private final UploadedImageRepository uploadImageRepository;
    private final StorageService storageService;

    /**
     * 매일 새벽 3시에 실행 (초 분 시 일 월 요일)
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteUnusedImages() {

        log.info("스케쥴러 시작됨");
        List<UploadedImage> unusedImages = uploadImageRepository.findByIsUsedFalse();

        for (UploadedImage image : unusedImages) {

            try{
                //실제 파일 삭제
                storageService.deleteImageFile(image.getImageUrl());

                //DB에서 삭제
                uploadImageRepository.delete(image);

            }catch (Exception e) {
                log.info("파일삭제 실패 = {}",image.getImageUrl());
            }
        }
    }

}
