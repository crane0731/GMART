package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.AccountActiveStatus;
import gmart.gmart.domain.enums.MannerGrade;
import gmart.gmart.domain.enums.MemberRole;
import gmart.gmart.dto.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보
 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Comment("로그인 아이디 : 이메일")
    @Column(name = "login_id",nullable = false,unique = true)
    private String loginId;

    @Comment("로그인 비밀번호")
    @Column(name = "password" ,nullable = false)
    private String password;

    @Comment("회원 이름")
    @Column(name = "name" ,nullable = false)
    private String name;

    @Comment("닉네임")
    @Column(name = "nickname" ,nullable = false,unique = true)
    private String nickname;

    @Comment("전화 번호")
    @Column(name = "phone_number" ,nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Comment("건머니")
    @Column(name = "g_money" ,nullable = false)
    private Long gMoney=0L;

    @Comment("건포인트")
    @Column(name = "g_point" ,nullable = false)
    private Long gPoint=0L;

    @Comment("매너포인트")
    @Column(name = "manner_point" ,nullable = false)
    private Long mannerPoint;

    @Comment("매너 등급")
    @Enumerated(EnumType.STRING)
    @Column(name = "manner_grade",nullable = false)
    private MannerGrade mannerGrade;

    @Comment("총 사용한 금액")
    @Column(name = "total_spent",nullable = false)
    private Long totalSpent=0L;

    @Comment("신고당한 수")
    @Column(name = "reported_count",nullable = false)
    private Long reportedCount=0L;

    @Comment("리뷰 수")
    @Column(name = "reviewed_count",nullable = false)
    private Long reviewedCount=0L;

    @Comment("계정 활성화 여부")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_active_status",nullable = false)
    private AccountActiveStatus accountActiveStatus;

    @Comment("회원 권환")
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role",nullable = false)
    private MemberRole memberRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberProfileImage memberProfileImage=new MemberProfileImage();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGundamGrade> memberGundamGrades = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> memberCoupons = new ArrayList<>();


    /**
     * 회원 생성 로직
     */
    public static Member createEntity(SignUpRequestDto dto,String encodedPassword){

        Address address = createAddress(dto);

        Member member = new Member();

        member.loginId = dto.getLoginId();
        member.password = encodedPassword;
        member.name = dto.getName();
        member.nickname = dto.getNickname();
        member.phoneNumber = dto.getPhone();
        member.memberProfileImage=null;
        member.address = address;
        member.gMoney=0L;
        member.gPoint=0L;
        member.mannerPoint=25L;
        member.mannerGrade = MannerGrade.NORMAL;
        member.totalSpent = 0L;
        member.reportedCount = 0L;
        member.reviewedCount = 0L;
        member.accountActiveStatus = AccountActiveStatus.Active;
        member.memberRole = MemberRole.MEMBER;


        return member;
    }

    //==Address 생성==//
    private static Address createAddress(SignUpRequestDto dto) {
        return Address.builder()
                .address(dto.getAddress())
                .addressDetails(dto.getAddressDetail())
                .zipCode(dto.getZipcode())
                .build();
    }

    /**
     * 프로필 이미지 등록
     */
    public void settingProfileImage(MemberProfileImage memberProfileImage) {
        this.memberProfileImage = memberProfileImage;
        memberProfileImage.setMember(this);
    }


}
