package gmart.gmart.service;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.inquiry.CreateInquiryRequestDto;
import gmart.gmart.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 문의 서비스
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final MemberService memberService; //회원 서비스

    private final InquiryRepository inquiryRepository;//문의 레파지토리

    /**
     * 문의를 생성하고 등록하는 서비스
     */
    @Transactional
    public void createInquiry(CreateInquiryRequestDto requestDto) {

        //로그인한 회원
        Member member = memberService.findBySecurityContextHolder();

        //문의 객체 생성 + 저장
        Inquiry.create(member,requestDto.getTitle(), requestDto.getContent());

    }

}
