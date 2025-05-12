package gmart.gmart.repository;

import gmart.gmart.domain.MemberProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 프로필 이미지 레파지토리
 */

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {
}
