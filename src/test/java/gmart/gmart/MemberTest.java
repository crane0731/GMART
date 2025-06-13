package gmart.gmart;


import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.MemberRole;
import gmart.gmart.dto.AddressDto;
import gmart.gmart.dto.SignUpRequestDto;
import gmart.gmart.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(false)  // 롤백 비활성화
public class MemberTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void test() {
        String encode = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);

        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setLoginId("admin@naver.com");
        dto.setPassword("1234");
        dto.setPasswordCheck("1234");
        dto.setName("괸리자");
        dto.setNickname("관리자");
        dto.setPhone("01039078318");

        AddressDto addressDto = new AddressDto();
        addressDto.setAddress("탄중로 518");
        addressDto.setAddressDetail("에이스 10차 아파트 103동 1804호");
        addressDto.setZipCode("10347");



        dto.setAddress(addressDto);
        dto.setProfileImageUrl(null);

        Member member = Member.createEntity(dto, encode);
        member.setMemberRole(MemberRole.ADMIN);

        memberRepository.save(member);
    }


    //$2a$10$ERE34nR0PMaTn.ZU/4Xj.OhKlU79Ll9HwaKagPQLxt.1Xi0pFIceu
    //$2a$10$.1RQGz4B4QRocPsXMWOCvuvXzpeJE1k6B9eKHvq4G74AmqXW3g8Iq
}
