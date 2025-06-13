//package gmart.gmart.service.image;
//
//import gmart.gmart.domain.MemberProfileImage;
//import gmart.gmart.repository.MemberProfileImageRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * 회원 프로필 이미지 서비스
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional(readOnly = true)
//public class MemberProfileImageService {
//
//    private final MemberProfileImageRepository memberProfileImageRepository;
//
//    /**
//     * 프로필 이미지 저장
//     */
//    @Transactional
//    public void save(MemberProfileImage memberProfileImage) {
//        memberProfileImageRepository.save(memberProfileImage);
//    }
//
//    /**
//     * 프로필 이미지 삭제
//     */
//    @Transactional
//    public void delete(MemberProfileImage memberProfileImage) {
//        memberProfileImageRepository.delete(memberProfileImage);
//    }
//
//
//
//
//}
