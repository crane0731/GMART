package gmart.gmart.service.admin;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.MemberProfileImage;
import gmart.gmart.domain.MemberSuspension;
import gmart.gmart.domain.UploadedImage;
import gmart.gmart.dto.member.MemberInfoResponseDto;
import gmart.gmart.dto.member.MemberSuspensionRequestDto;
import gmart.gmart.dto.mybatis.MemberListResponseDto;
import gmart.gmart.dto.mybatis.SearchMemberListDto;
import gmart.gmart.exception.CustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.mybatis.MybatisMemberRepository;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.member.MemberSuspensionService;
import gmart.gmart.service.token.RefreshTokenService;
import gmart.gmart.service.image.MemberProfileImageService;
import gmart.gmart.service.image.UploadMemberProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 회원 관리 서비스
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminMemberService {

    private final String DEFAULT_PROFILE_IMAGE_URL = "/2a566036-d5b7-4996-b040-aa43253191dc.png";

    private final MemberService memberService;
    private final MemberProfileImageService memberProfileImageService;
    private final UploadMemberProfileImageService profileImageService;
    private final RefreshTokenService refreshTokenService;
    private final MemberSuspensionService memberSuspensionService;
    private final MybatisMemberRepository mybatisMemberRepository;

    /**
     * 조건에 따른 회원 리스트 조회
     * @param requestDto 필터링 조회 요청 DTO
     * @return 회원 리스트
     */
    public List<MemberListResponseDto> findAll(SearchMemberListDto requestDto){

        return mybatisMemberRepository.findAll(requestDto);
    }


    /**
     * 회원 상세 조회
     * @param memberId 회원아이디
     * @return MemberInfoResponseDto DTO
     */
    public MemberInfoResponseDto findMemberDetailInfo(Long memberId){

        //회원 아이디로 회원 조회
        Member member = memberService.findById(memberId);

        //DTO 반환
        return MemberInfoResponseDto.createDto(member);

    }

    /**
     * 회원 삭제 서비스
     * 회원 삭제시 회원프로필 이미지(기본 이미지 제외)를 함께 삭제
     * 회원 삭제시 해당 회원의 리프레쉬 토큰을 삭제
     * 회원의 엑세스 토큰은 관리자가 회원들의 엑세스 토큰을 알지 못하므로 블랙리스트 등록 불가(대신 토큰 유효시간을 짧게 설정)
     * @param memberId
     */
    @Transactional
    public void deleteMember(Long memberId){

        //회원 아이디로 회원 조회
        Member member = memberService.findById(memberId);

        //회원 삭제 진행
        processingDelete(member);
    }

    /**
     * 회원 일시 정지
     * @param memberId 회원 아이디
     * @param dto 회원정지요청 DTO
     */
    @Transactional
    public void suspensionMember(Long memberId , MemberSuspensionRequestDto dto){

        //회원 찾기
        Member member = memberService.findById(memberId);

        //일시정지 진행
        processingSuspensionMember(dto, member);
    }

    /**
     * 회원 일시 정지 해제
     * @param memberId 회원 아이디
     */
    @Transactional
    public void releaseMember(Long memberId){

        //회원 찾기
        Member member = memberService.findById(memberId);

        //일시 정지 해체 처리
        member.getMemberSuspensions().forEach(MemberSuspension::releaseMemberActiveStatus);

    }

    //==회원 계정 일시정지 진행 로직==//
    private void processingSuspensionMember(MemberSuspensionRequestDto dto, Member member) {

        //이미 정지중인 회원인지 확인
        if(memberSuspensionService.isCurrentlySuspended(member)){
            throw new CustomException(ErrorMessage.ALREADY_SUSPENDED_MEMBER);
        }

        //회원일시정지 엔티티 객체 생성
        MemberSuspension memberSuspension = MemberSuspension.createEntity(member, dto.getReason(), dto.getDay());

        //저장
        memberSuspensionService.save(memberSuspension);

        //회원 제재 수 증가
        member.upSuspensionCount();

    }


    //==회원 삭제 로직==//
    private void processingDelete(Member member) {
        //회원 프로필 이미지 조회
        MemberProfileImage memberProfileImage = member.getMemberProfileImage();

        //회원 사용자 프로필 이미지 삭제
        deleteProfileImageIfNotDefault(memberProfileImage);

        //회원 삭제
        memberService.delete(member);
        
        //리프레쉬 토큰 삭제
        refreshTokenService.delete(member);
    }


    //==사용자 프로필 이미지 삭제==//
    private void deleteProfileImageIfNotDefault(MemberProfileImage memberProfileImage) {

        //프로필 이미지가 기본이미지가 아니라면 회원 프로필 이미지 삭제
        //프로필 이미지가 기본이미지라면 그냥 회원만 삭제
        if(!memberProfileImage.getImageUrl().equals(DEFAULT_PROFILE_IMAGE_URL)) {
            memberProfileImageService.delete(memberProfileImage);

            //업로드된 이미지 조회
            UploadedImage uploadedImage = profileImageService.findByImageUrl(memberProfileImage.getImageUrl());

            uploadedImage.usedFalse();
        }
    }

}
