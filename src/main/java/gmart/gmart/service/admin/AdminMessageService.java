package gmart.gmart.service.admin;

import gmart.gmart.domain.AdminMessage;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.adminmessage.AdminMessageListResponseDto;
import gmart.gmart.dto.adminmessage.CreateAdminMessageRequestDto;
import gmart.gmart.dto.adminmessage.SearchAdminMessageCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.AdminMessageCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.adminmessage.AdminMessageRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 메시지 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMessageService {

    public final MemberService memberService; //회원 서비스
    private final AdminMessageRepository adminMessageRepository; //관리자 메시지 레파지토리

    /**
     * [서비스 로직]
     * 관리자- 특정 회원에게 메시지를 전송 + 저장
     * @param memberId 회원 아이디
     * @param requestDto 관리자 메시지 등록 DTO
     */
    @Transactional
    public void createMessage(Long memberId, CreateAdminMessageRequestDto requestDto){

        //메시지를 보낼 회원 조회
        Member member = memberService.findById(memberId);

        //메시지 생성 + 저장
        sendMessage(requestDto, member);
    }

    /**
     * [서비스 로직]
     * 관리자 - 모든 회원에게 메시지를 전송 + 저장
     * @param requestDto 관리자 메시지 등록 DTO
     */
    @Transactional
    public void createMessageForAllMembers(CreateAdminMessageRequestDto requestDto){

        //전체 회원 조회 + 메시지 생성 + 저장
        memberService.findAll()
                .forEach(member -> sendMessage(requestDto, member));
    }

    /**
     * [서비스 로직]
     * 관리자 - 관리자 메시지 논리작 삭제 (SOFT DELETE)
     * @param adminMessageId 관리자 메시지 아이디
     */
    @Transactional
    public void softDelete(Long adminMessageId){

        //삭제 처리할 관리자 메시지 조회
        AdminMessage adminMessage = findById(adminMessageId);

        //논리적 삭제 처리
        adminMessage.softDelete();

    }

    /**
     * [서비스 로직]
     * 관리자 - 특정 회원의 관리자 메시지 목록 조회
     * @param memberId 회원 아이디
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<AdminMessageListResponseDto> 페이징된 응답 DTO 리스트
     */
    public PagedResponseDto<AdminMessageListResponseDto> findAllByCond(Long memberId,SearchAdminMessageCondDto condDto){
        
        //회원 조회
        Member member = memberService.findById(memberId);

        //페이징된 관리자 메시지 조회 + 응답 DTO 생성 + 반환
        return getPagedAdminMessages(condDto, member);

    }

    /**
     * [서비스 로직]
     * 로그인한 회원이 자신이 받은 관리자 메시지 목록을 조회
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<AdminMessageListResponseDto> 페이징된 응답 DTO 리스트
     */
    public PagedResponseDto<AdminMessageListResponseDto> findMyAdminMessage(SearchAdminMessageCondDto condDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //페이징된 관리자 메시지 조회 + 응답 DTO 생성 + 반환
        return getPagedAdminMessages(condDto, member);

    }

    /**
     * [저장]
     * @param adminMessage 관리자 메시지 엔티티
     */
    @Transactional
    public void save(AdminMessage adminMessage){
        adminMessageRepository.save(adminMessage);
    }

    /**
     * [삭제]
     * @param adminMessage 관리자 메시지 엔티티
     */
    @Transactional
    public void delete(AdminMessage adminMessage){
        adminMessageRepository.delete(adminMessage);
    }

    /**
     * [조회]
     * ID(PK) 값으로 단일 조회
     * @param id 관리자 메시지 아이디
     * @return AdminMessage 관리자 메시지 엔티티
     */
    public AdminMessage findById(Long id){
        return adminMessageRepository.findById(id).orElseThrow(()->new AdminMessageCustomException(ErrorMessage.NOT_FOUND_ADMIN_MESSAGE));
    }

    //==페이징된 관리자 메시지 조회 + 응답 DTO 생성 + 반환 하는 로직==//
    private PagedResponseDto<AdminMessageListResponseDto> getPagedAdminMessages(SearchAdminMessageCondDto condDto, Member member) {
        Page<AdminMessage> page = adminMessageRepository.findAllByCond(member, condDto, createPageable());

        List<AdminMessageListResponseDto> content = page.getContent().stream()
                .map(AdminMessageListResponseDto::create)
                .toList();

        //페이징응답 DTO 생성 + 반환
        return createPagedResponseDto(content, page);
    }

    //==페이징응답 DTO 생성 + 반환 메서드==//
    private PagedResponseDto<AdminMessageListResponseDto> createPagedResponseDto(List<AdminMessageListResponseDto> content, Page<AdminMessage> page) {
        return PagedResponseDto.<AdminMessageListResponseDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }


    //==페이징 생성 메서드==//
    private Pageable createPageable() {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(0, 10);
    }



    //==메시지 생성 + 저장 메서드==//
    private void sendMessage(CreateAdminMessageRequestDto requestDto, Member member) {
        //메시지 생성
        AdminMessage adminMessage = AdminMessage.create(requestDto.getContent(), requestDto.getMessageType());

        //메시지에 보낼 회원 세팅
        adminMessage.setMember(member);

        //메시지 저장
        adminMessageRepository.save(adminMessage);
    }



}
