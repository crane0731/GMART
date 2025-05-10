package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.UploadPurpose;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 이미지 임시 업로드용 테이블 엔티티
 */
@Entity
@Table(name = "uploaded_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadedImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploaded_image_id")
    private Long id;

    @Comment("파일이름")
    @Column(name = "file_name", nullable = false)
    private String fileName;


    @Comment("이미지 URL")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @Comment("이미지 사용 여부")
    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Comment("이미지 목적")
    @Column(name = "purpose", nullable = false)
    @Enumerated(EnumType.STRING)
    private UploadPurpose purpose; // 예: PROFILE, POST 등

    /**
     * 엔티티 생성 메서드
     */
    public static UploadedImage createEntity(String fileName, String imageUrl , UploadPurpose purpose) {
        UploadedImage image = new UploadedImage();
        image.fileName = fileName;
        image.imageUrl = imageUrl;
        image.isUsed = false;
        image.purpose = purpose;
        return image;
    }

    /**
     * 이미지 사용 여부 처리 (true)
     */
    public void usedTrue(){
        isUsed = true;
    }

    /**
     * 이미지 사용 여부 처리 (false)
     */
    public void usedFalse(){
        isUsed = false;
    }


}
