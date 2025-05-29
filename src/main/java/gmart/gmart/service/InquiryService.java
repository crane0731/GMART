package gmart.gmart.service;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.inquiry.*;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.InquiryCustomException;
import gmart.gmart.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

        //문의 객체 생성
        Inquiry inquiry = Inquiry.createEntity(member, requestDto.getTitle(), requestDto.getContent());

        //문의 저장
        save(inquiry);
    }

    /**
     * 문의(제목, 내용)를 수정하는 서비스
     * @param inquiryId 문의 아이디(PK)
     * @param requestDto  수정 요청 DTO
     */
    @Transactional
    public void updateInquiry(Long inquiryId,UpdateInquiryRequestDto requestDto) {

        //문의 조회
        Inquiry inquiry = findById(inquiryId);

        //문의가 현재 로그인한 회원이 쓴건지 확인(아니라면 예외를 던짐)
        validateInquiryOwner(inquiry);

        //업데이트
        inquiry.update(requestDto.getTitle(), requestDto.getContent());

    }

    /**
     * 문의 삭제 서비스
     * @param inquiryId 문의 아이디(PK)
     */
    @Transactional
    public void deleteInquiry(Long inquiryId) {

        //문의 조회
        Inquiry inquiry = findById(inquiryId);

        //문의가 현재 로그인한 회원이 쓴건지 확인(아니라면 예외를 던짐)
        validateInquiryOwner(inquiry);

        //삭제
        delete(inquiry);

    }

    /**
     * 조회 서비스
     * @param inquiryId 문의 아이디(PK)
     * @return 응답 DTO
     */
    public InquiryResponseDto getInquiry(Long inquiryId) {

        //조회
        Inquiry inquiry = findById(inquiryId);

        //DTO 생성 및 반환
        return InquiryResponseDto.createDto(inquiry);
    }

    /**
     * 검색 조건에 따른 리스트 조회 서비스
     * @param cond 검색 조건
     * @return 커스텀 페이징된 응답 DTO
     */
    public PagedResponseDto<InquiryListResponseDto> getAllInquiry(SearchInquiryCondDto cond) {

        //검색 조건에 따라 문의 리스트 조회 + 페이징
        Page<Inquiry> page = findAllBycond(cond);

        //DTO로 변환
        List<InquiryListResponseDto> content = createInquiryListResponseDtos(page);

        //페이징 응답 DTO 생성 + 반환
        return createPagedResponseDto(content, page);
    }

    /**
     * 삭제
     */
    @Transactional
    public void delete(Inquiry inquiry) {
        inquiryRepository.delete(inquiry);
    }

    /**
     * 단일 조회
     */
    public Inquiry findById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElseThrow(()->new InquiryCustomException(ErrorMessage.NOT_FOUND_INQUIRY));
    }

    /**
     * 문의 저장 메서드
     * @param inquiry 문의 객체
     */
    @Transactional
    public void save(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }


    //==문의가 현재 로그인한 회원이 쓴건지 확인(아니라면 예외를 던짐)==//
    private void validateInquiryOwner(Inquiry inquiry) {
        //로그인한 회원
        Member member = memberService.findBySecurityContextHolder();


        //문의가 현재 로그인한 회원이 쓴건지 확인(아니라면 예외를 던짐)
        if(!Objects.equals(member.getId(), inquiry.getMember().getId())){
            throw new InquiryCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<InquiryListResponseDto> createPagedResponseDto(List<InquiryListResponseDto> content, Page<Inquiry> page) {
        return new PagedResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }

    //==페이지 객체를 List<InquiryListResponseDto> 로 변환==//
    private List<InquiryListResponseDto> createInquiryListResponseDtos(Page<Inquiry> page) {
        List<InquiryListResponseDto> content = page.getContent()
                .stream()
                .map(InquiryListResponseDto::createDto)
                .toList();
        return content;
    }

    //==페이징 생성 메서드==//
    private Pageable createPageable() {
        Pageable pageable = PageRequest.of(0, 10); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==검색 조건에 따라 문의 리스트 조회 + 페이징 하는 로직==//
    private Page<Inquiry> findAllBycond(SearchInquiryCondDto cond) {
        //페이징 생성
        Pageable pageable = createPageable();

        //조회
        Page<Inquiry> page = inquiryRepository.findAllByCond(cond, pageable);
        return page;
    }





}
