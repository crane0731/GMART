package gmart.gmart.service;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.userdetail.CustomUserDetails;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.MemberRepository;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetails를 생성하는 서비스 -> Spring Security에 필요
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;


    /**
     * 사용자 이름(loginId)으로 사용자의 정보를 가져오는 메서드
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()->new UsernameNotFoundException(ErrorMessage.NOT_FOUND_MEMBER));
        return new CustomUserDetails(member);
    }


}
