package gmart.gmart.service.member;

import gmart.gmart.domain.*;
import gmart.gmart.domain.userdetail.CustomUserDetails;
import gmart.gmart.dto.LoginRequestDto;
import gmart.gmart.dto.MemberGundamGradeListDto;
import gmart.gmart.dto.MemberPreferredGundamGradeDto;
import gmart.gmart.dto.SignUpRequestDto;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.dto.member.UpdateMemberInfoRequestDto;
import gmart.gmart.dto.password.ChangePasswordRequestDto;
import gmart.gmart.dto.token.TokenResponseDto;
import gmart.gmart.exception.CustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.MemberRepository;
import gmart.gmart.service.token.RefreshTokenService;
import gmart.gmart.service.token.TokenService;
import gmart.gmart.service.image.UploadMemberProfileImageService;
import gmart.gmart.service.redis.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final UploadMemberProfileImageService profileImageService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final TokenBlackListService tokenBlackListService;
    private final MemberSuspensionService memberSuspensionService;

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 회원 삭제
     * @param member
     */
    @Transactional
    public void delete(Member member){
        memberRepository.delete(member);
    }

    /**
     * 회원 탈퇴
     * @param request
     */
    @Transactional
    public void withdrawMember(HttpServletRequest request){

        //로그인한 회원 조회
        Member member = findBySecurityContextHolder();

        //회원 삭제
        processingDelete(request, member);

    }

    /**
     * 회원정보 수정
     * @param dto
     */
    @Transactional
    public void updateMemberInfo(UpdateMemberInfoRequestDto dto) {

        //업데이트 체킹
        updateCheck(dto);

        //현재 로그인된 회원 조회
        Member member = findBySecurityContextHolder();

        //회원정보 업데이트
        processingUpdate(dto, member);

        

    }


    /**
     * 비밀번호 수정
     * @param dto
     */
    @Transactional
    public void updatePassword(ChangePasswordRequestDto dto) {

        //현재 로그인된 회원 조회
        Member member = findBySecurityContextHolder();

        //기존 비밀번호가 유효한지 검사
        validateOldPassword(dto.getOldPassword(), member.getPassword());

        //새로운 패스워드 , 패스워드 체크 일치 검사
        passwordCheck(dto.getNewPassword(), dto.getNewPasswordCheck());

        //새로운 패스워드로 비밀번호 변경
        member.updatePassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));

    }


    //==기존 비밀번호가 유효한지 검사하는 로직==//
    private void validateOldPassword(String dtoPassword, String memberPassword) {
        if(dtoPassword.equals(bCryptPasswordEncoder.encode(memberPassword))){
            throw new CustomException(ErrorMessage.INCORRECT_PASSWORD);
        }
    }


    /**
     * 선호하는 건담 등급 등록 or 변경
     * @param dto
     */
    @Transactional
    public void updateGundamGrade(MemberGundamGradeListDto dto){

        //현재 로그인한 회원 조회
        Member member = findBySecurityContextHolder();

        //회원-선호건담등급 객체 생성
        List<MemberGundamGrade> memberGundamGrades = getMemberGundamGrades(dto);

        //업데이트
        member.updateMemberGundamGrade(memberGundamGrades);
        
    }

    /**
     * 나의 회원정보 조회
     * @return
     */
    public MemberInfoResponseDto getMyMemberInfo(){

        //현재 로그인한 회원 조회
        Member member = findBySecurityContextHolder();

        //회원 정보 생성해서 반환
        return MemberInfoResponseDto.createDto(member);

    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(HttpServletRequest request){

        //현재 로그인한 회원 조회
        Member member = findBySecurityContextHolder();

        //리프레쉬 토큰 삭제
        deleteToken(request, member);

    }


    /**
     * 로그인된 회원 정보 가져오기 (Spring Security)
     * @return
     */
    public Member findBySecurityContextHolder(){

        //시큐리티 컨텍스트 홀더에서 인증 객체 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //설정한 username (loginId) 조회
        String loginId = authentication.getName();

        // Member 반환
        return findByLoginId(loginId);

    }


    /**
     * 로그인 서비스
     * @param dto
     * @return
     */
    @Transactional
    public TokenResponseDto loginMember(LoginRequestDto dto){

        //스프링 시큐리티 수동 로그인
        Member loginMember = securityLogin(dto);

        //정지된 계정인지 확인
        validateNotSuspended(loginMember);

        //JWT 토큰 생성 , 반환
        return makeToken(loginMember);

    }


    /**
     * Login ID를 통해 회원을 조회
     * @return
     */
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_MEMBER));
    }


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
        UploadedImage uploadedImage = getUploadedImage(signUpRequestDto.getProfileImageUrl());

        //회원 생성
        Member member = createMember(signUpRequestDto, uploadedImage);

        //회원 저장
        memberRepository.save(member);

    }

    /**
     * [조회]
     * 모든 회원 조회
     * @return  List<Member> 회원 엔티티 리스트
     */
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //==계정 정지당한 회원인지 확인하는 로직==//
    private void validateNotSuspended(Member member) {
        if (memberSuspensionService.isCurrentlySuspended(member)) {
            throw new CustomException(ErrorMessage.ALREADY_SUSPENDED_MEMBER);
        }
    }

    //==회원 생성 + 프로필 이미지 세팅==//
    private Member createMember(SignUpRequestDto signUpRequestDto, UploadedImage uploadedImage) {
        //패스워드 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(signUpRequestDto.getPassword());

        signUpRequestDto.setProfileImageUrl(uploadedImage.getImageUrl());

        //회원 생성
        Member member = Member.createEntity(signUpRequestDto,encodedPassword);


        return member;
    }

    //==업로드된 이미지 조회 ==//
    private UploadedImage getUploadedImage(String profileImageUrl) {

        if(profileImageUrl!=null) {
            //업로드 이미지 조회
            UploadedImage uploadedImage = profileImageService.findByImageUrl(profileImageUrl);

            //이미지 사용 처리
            if(!uploadedImage.isUsed()) {
                uploadedImage.usedTrue();
            }

            return uploadedImage;
        }else {
            //프로필 이미지가 null 일 경우 기본 프로필 이미지 사용
            return profileImageService.findDefaultProfileImage();
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

    //==회원-선호건담등급 객체 생성 ==//
    private List<MemberGundamGrade> getMemberGundamGrades(MemberGundamGradeListDto dto) {

        //리스트 생성
        List<MemberGundamGrade> memberGundamGrades = new ArrayList<>();

        if(dto.getGundamGrades() !=null) {
            for (MemberPreferredGundamGradeDto gundamGrade : dto.getGundamGrades()) {

                //회원-선호건담등급 객체 생성
                MemberGundamGrade memberGundamGrade = MemberGundamGrade.createEntity(gundamGrade.getGundamGrade());

                //리스트에 추가
                memberGundamGrades.add(memberGundamGrade);
            }
        }
        return memberGundamGrades;
    }

    //==스프링 시큐리티 수동 로그인==//
    private Member securityLogin(LoginRequestDto dto) {
        //UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());


        //AuthenticationManager로 인증 시도 (loadUser + password 체크 내부 수행)
        Authentication authentication = authenticationManager.authenticate(authToken);



        //인증 정보 SecurityContext에 저장 (로그인 처리)
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //로그인된 회원 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        return findByLoginId(userDetails.getUsername());


    }

    //==리프레쉬 토큰 + 엑세스 토큰 생성==//
    private TokenResponseDto makeToken(Member loginMember) {
        //리프레시 토큰 생성
        String refreshToken = refreshTokenService.createRefreshToken(loginMember);

        //엑세스 토큰 생성
        String accessToken = tokenService.createNewAccessToken(refreshToken);

        //응답 DTO 반환
        TokenResponseDto responseDto = TokenResponseDto.createDto(accessToken, refreshToken);
        return responseDto;
    }

    //==엑세스 토큰 블랙리스트 등록==//
    private void setBlackListAccessToken(HttpServletRequest request) {
        //요청에서 인증 헤더 추출
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);

            //블랙리스트에 등록
            tokenBlackListService.blackList(accessToken);
        }
    }

    //==회원 정보 업데이트 로직==//
    private void processingUpdate(UpdateMemberInfoRequestDto dto, Member member) {

        //기본 프로필 이미지 가져오기
        UploadedImage defaultProfileImage = profileImageService.findDefaultProfileImage();

        //회원의 기존 프로필 이미지 조회
        String oldImage = member.getProfileImageUrl();

        //기존 프로필 이미지가 기본이미지가 아니라면 예전 업로드 이미지 비활성화
        if(!oldImage.equals(defaultProfileImage.getImageUrl())) {

            UploadedImage oldUploadImage = getUploadedImage(oldImage);
            oldUploadImage.usedFalse();
        }

        member.update(dto);

    }

    //==회원 정보 수정에 필요한 검증 로직==//
    private void updateCheck(UpdateMemberInfoRequestDto dto) {
        //닉네임 중복 검사
        nicknameDuplicatedCheck(dto.getNickname());

        //전화번호 중복 검사
        phoneDuplicatedCheck(dto.getPhone());
    }

    //==회원 삭제 로직==//
    private void processingDelete(HttpServletRequest request, Member member) {

        //기본 프로필 이미지 가져오기
        UploadedImage defaultProfileImage = profileImageService.findDefaultProfileImage();



        //프로필 이미지가 기본이미지가 아니라면 회원 프로필 이미지 삭제
        if(!member.getProfileImageUrl().equals(defaultProfileImage.getImageUrl())) {


            //업로드된 이미지 조회
            UploadedImage uploadedImage = profileImageService.findByImageUrl(member.getProfileImageUrl());

            uploadedImage.usedFalse();

        }
        //회원 삭제
        delete(member);

        //토큰 삭제(엑세스, 리프레쉬)
        deleteToken(request, member);
    }

    //==토큰 삭제 처리 로직==//
    private void deleteToken(HttpServletRequest request, Member member) {
        //리프레쉬 토큰 삭제
        refreshTokenService.delete(member);

        //엑세스 토큰 블랙리스트 등록
        setBlackListAccessToken(request);
    }



}
