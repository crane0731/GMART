package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 회원 프로필 이미지
 */
@Entity
@Table(name = "member_profile_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_image_id")
    private Long id;

    @Comment("이미지 URL")
    @Column(name = "image_url",nullable = false)
    private String imageUrl;

    /**
     * 엔티티 생성 메서드
     */
    public static MemberProfileImage createEntity( String imageUrl) {
        MemberProfileImage memberProfileImage = new MemberProfileImage();
        memberProfileImage.imageUrl = imageUrl;
        return memberProfileImage;
    }


    /**
     * 이미지 URL 설정
     */
    protected void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
