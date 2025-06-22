package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 카카오 엑세스 토큰을 보관하기 위한 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kakao_access_token")
public class KakaoAccessToken extends BaseTimeEntity {

    @Comment("카카오 엑세스 토큰 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kakao_access_token_id")
    private Long id;

    @Comment("회원 아이디")
    @Column(name = "member_id")
    private Long memberId;

    @Comment("엑세스 토큰")
    @Column(name = "access_token")
    private String accessToken;


    /**
     * [생성 메서드]
     * @param memberId 회원 아이디
     * @param accessToken 카카오 엑세스 토큰
     * @return KakaoAccessToken 카카오 엑세스 토큰 엔티티
     */
    public static KakaoAccessToken create(Long memberId,String accessToken){
        KakaoAccessToken kakaoAccessToken= new KakaoAccessToken();
        kakaoAccessToken.memberId=memberId;
        kakaoAccessToken.accessToken=accessToken;
        return kakaoAccessToken;
    }

}
