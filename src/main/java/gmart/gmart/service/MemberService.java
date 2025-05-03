package gmart.gmart.service;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.MemberProfileImage;
import gmart.gmart.domain.UploadedImage;
import gmart.gmart.dto.SignUpRequestDto;
import gmart.gmart.exception.CustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.MemberRepository;
import gmart.gmart.service.image.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProfileImageService profileImageService;

    private final String DEFAULT_PROFILE_IMAGE_URL = "/2a566036-d5b7-4996-b040-aa43253191dc.png";

    /**
     * ID 를 통해 회원을 조회
     * @param memberId
     * @return
     */
    public Member findById(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorMessage.NOT_FOUND_MEMBER));
    }

    /**
     * 회원가입
     * @param signUpRequestDto
     */
    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto){

        //회원 가입을 위한 검증
        signupcheck(signUpRequestDto);

        //==검증을 무사히 마치면 회원 가입 진행==//

        //업로드된 이미지 조회
        UploadedImage uploadedImage = getUploadedImage(signUpRequestDto);

        //회원 생성 + 프로필 이미지 세팅
        Member member = createMemberWithProfileImage(signUpRequestDto, uploadedImage);

        //회원 저장
        memberRepository.save(member);

    }

    //==회원 생성 + 프로필 이미지 세팅==//
    private Member createMemberWithProfileImage(SignUpRequestDto signUpRequestDto, UploadedImage uploadedImage) {
        //패스워드 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(signUpRequestDto.getPassword());

        //회원 생성
        Member member = Member.createEntity(signUpRequestDto,encodedPassword);

        //회원 프로필 이미지 생성
        MemberProfileImage memberProfileImage = MemberProfileImage.createEntity(uploadedImage.getImageUrl());

        //회원 - 프로필 이미지 세팅
        member.settingProfileImage(memberProfileImage);
        return member;
    }

    //==업로드된 이미지 조회 ==//
    private UploadedImage getUploadedImage(SignUpRequestDto signUpRequestDto) {

        if(signUpRequestDto.getProfileImageUrl()!=null) {
            //업로드 이미지 조회
            UploadedImage uploadedImage = profileImageService.findByImageUrl(signUpRequestDto.getProfileImageUrl());

            //이미지 사용 처리
            uploadedImage.usedTrue();
            return uploadedImage;
        }else {
            //프로필 이미지가 null 일 경우 기본 프로필 이미지 사용
            return profileImageService.findByImageUrl(DEFAULT_PROFILE_IMAGE_URL);
        }

    }

    //==회원가입 검증 로직==//
    private void signupcheck(SignUpRequestDto signUpRequestDto) {
        //아이디 중복 검사
        loginDuplicatedCheck(signUpRequestDto.getLoginId());

        //비밀번호 일치 검사
        passwordCheck(signUpRequestDto.getPassword(), signUpRequestDto.getPasswordCheck());

        //닉네임 중복 검사
        nicknameDuplicatedCheck(signUpRequestDto.getNickname());

        //전화번호 중복 검사
        phoneDuplicatedCheck(signUpRequestDto.getPhone());
    }

    //==전화번호 중복 검사 로직==//
    private void phoneDuplicatedCheck(String phone) {
        if(memberRepository.findByPhoneNumber(phone).isPresent()){
            throw new CustomException(ErrorMessage.DUPLICATED_PHONE);
        }
    }

    //==닉네임 중복 검사 로직 ==//
    private void nicknameDuplicatedCheck(String nickname) {
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new CustomException(ErrorMessage.DUPLICATED_NICKNAME);
        }

    }

    //==비밀번호 일치 검사 로직==//
    private void passwordCheck(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)){
            throw new CustomException(ErrorMessage.PASSWORD_MISMATCH);
        }
    }

    //==아이디 중복 검사 로직 ==//
    private void loginDuplicatedCheck(String loginId) {
        if(memberRepository.findByLoginId(loginId).isPresent()){
            throw new CustomException(ErrorMessage.DUPLICATED_LOGIN_ID);
        }
    }

}
