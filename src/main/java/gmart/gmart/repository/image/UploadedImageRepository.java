package gmart.gmart.repository.image;

import gmart.gmart.domain.UploadedImage;
import gmart.gmart.domain.enums.ImageDefaultStatus;
import gmart.gmart.domain.enums.UploadPurpose;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 임시 이미지 업로드 레파지토리
 */
public interface UploadedImageRepository extends JpaRepository<UploadedImage,Long> {

    /**
     * ImageUrl을 통해 UploadedImage 조회
     * @param imageUrl
     * @return
     */
    Optional<UploadedImage> findByImageUrl(String imageUrl);

    /**
     * isUsed = false 인 이미지를 조회
     * @return
     */
    @Query("select u from UploadedImage u where u.isUsed=false")
    List<UploadedImage> findByIsUsedFalse();

    @Query("SELECT u FROM UploadedImage u where u.defaultStatus=:defaultStatus and u.purpose=:uploadPurpose")
    Optional<UploadedImage>findDefaultMemberProfileImage(@Param("defaultStatus") ImageDefaultStatus defaultStatus ,@Param("uploadPurpose") UploadPurpose uploadPurpose);
}
