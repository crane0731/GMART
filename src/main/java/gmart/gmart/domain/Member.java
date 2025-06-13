package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.MannerGrade;
import gmart.gmart.domain.enums.MemberRole;
import gmart.gmart.domain.enums.ReportStatus;
import gmart.gmart.dto.AddressDto;
import gmart.gmart.dto.SignUpRequestDto;
import gmart.gmart.dto.member.UpdateMemberInfoRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GMoneyCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Comment("제재 당한 수")
    @Column(name = "suspension_count",nullable = false)
    private Long suspensionCount=0L;

    @Comment("리뷰 수")
    @Column(name = "reviewed_count",nullable = false)
    private Long reviewedCount=0L;

    @Setter
    @Comment("회원 권환")
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role",nullable = false)
    private MemberRole memberRole;

    @Comment("회원 프로필 이미지")
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGundamGrade> memberGundamGrades = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSuspension> memberSuspensions = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.PERSIST)
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteGundam> favoriteGundams = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeStore> likeStores = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<FavoriteStore> favoriteStores=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<FavoriteItem> favoriteItems=new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Store store;

    @OneToMany(mappedBy="buyer")
    private List<Order> buyOrders = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<Order> saleOrders = new ArrayList<>();





    /**
     * 회원 생성 로직
     */
    public static Member createEntity(SignUpRequestDto dto,String encodedPassword){

        Address address = createAddress(dto.getAddress());

        Member member = new Member();

        member.loginId = dto.getLoginId();
        member.password = encodedPassword;
        member.name = dto.getName();
        member.nickname = dto.getNickname();
        member.phoneNumber = dto.getPhone();
        member.profileImageUrl= dto.getProfileImageUrl();
        member.address = address;
        member.gMoney=0L;
        member.gPoint=0L;
        member.mannerPoint=25L;
        member.mannerGrade = MannerGrade.NORMAL;
        member.totalSpent = 0L;
        member.reportedCount = 0L;
        member.reviewedCount = 0L;
        member.suspensionCount=0L;
        member.memberRole = MemberRole.MEMBER;


        return member;
    }


    /**
     * [비즈니스 로직]
     * 신고 수 증가 +1
     */
    public void plusReportedCount(){
        reportedCount++;
    }



    /**
     * [비즈니스 로직]
     * 신고수 감소 -1
     */
    protected void minusReportedCount(){
        if(reportedCount>0) {
            reportedCount--;
        }
    }

    /**
     * [비즈니스 로직]
     * 건머니 충전
     * @param chargeMoney
     */
    public void chargeGMoney(Long chargeMoney){
        this.gMoney+=chargeMoney;
    }

    /**
     * [비즈니스 로직]
     * 건머니 환불
     * @param chargeMoney
     */
    public void refundGMoney(Long chargeMoney){

        if(this.gMoney-chargeMoney<0){
            throw new GMoneyCustomException(ErrorMessage.NOT_ENOUGH_GMONEY_TO_REFUND);
        }else{
            this.gMoney-=chargeMoney;
        }
    }

    /**
     * [비즈니스 로직]
     * 건포인트 충전
     * @param chargePoint 충전할 포인트
     * @return Long 충전 후 포인트 금액
     */
    public Long chargeGPoint(Long chargePoint){
        return this.gPoint+=chargePoint;
    }

    /**
     * [비즈니스 로직]
     * 건포인트 환불
     * @param refundPoint 회수할 포인트
     * @return Long 회수 후 포인트 금액
     */
    public Long refundGPoint(Long refundPoint){

        if(this.gPoint-refundPoint<0){
            return this.gPoint=0L;
        }else{
            return this.gPoint-=refundPoint;
        }
    }




//    /**
//     * 연관관계편의메서드 - 프로필 이미지 등록
//     */
//    public void addProfileImage(MemberProfileImage memberProfileImage) {
//        this.memberProfileImage = memberProfileImage;
////        memberProfileImage.setMember(this);
//    }


    /**
     * 연관관계편의메서드 - 선호 건담 등급 등록
     */
    public void updateMemberGundamGrade(List<MemberGundamGrade> gundamGrades) {
        this.memberGundamGrades.clear();
        this.memberGundamGrades.addAll(gundamGrades);

        for (MemberGundamGrade gundamGrade : gundamGrades) {
            gundamGrade.setMember(this);
        }
    }


    /**
     * 비밀번호 변경
     */
    public void updatePassword(String newPassword){
        this.password=newPassword;
    }


    /**
     * 회원 정보 수정
     */
    public void update(UpdateMemberInfoRequestDto dto){
        //주소 객체 생성
        Address address = createAddress(dto.getAddress());

        this.nickname=dto.getNickname();
        this.name = dto.getName();
        this.phoneNumber = dto.getPhone();
        this.address = address;
        this.profileImageUrl= dto.getProfileImageUrl();
    }

    /**
     * [비즈니스 로직]
     * 회원 제대 수 증가
     */
    public void upSuspensionCount(){
        this.suspensionCount++;
    }



    //==Address 생성==//
    private static Address createAddress(AddressDto dto) {
        return Address.createEntity(dto);
    }
}
