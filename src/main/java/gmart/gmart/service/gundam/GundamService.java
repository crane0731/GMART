package gmart.gmart.service.gundam;

import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.Inquiry;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import gmart.gmart.dto.inquiry.InquiryListResponseDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GundamCustomException;
import gmart.gmart.repository.gundam.GundamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  건담 정보 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GundamService {

    private final GundamRepository gundamRepository; //건담 정보 레파지토리


    /**
     * [서비스 로직]
     * 건담 정보 리스트 조회
     * @param condDto 검색 조건 DTO
     * @param page 페이지 번호
     * @return PagedResponseDto<GundamListResponseDto> 페이징된 응답 DTO 리스트
     */
    public PagedResponseDto<GundamListResponseDto> findAllByCond(SearchGundamCondDto condDto,int page){


        Page<Gundam> pageList = gundamRepository.findAllByCond(condDto, createPageable(page));

        List<GundamListResponseDto> content = pageList.getContent().stream().map(GundamListResponseDto::createDto).toList();

        return createPagedResponseDto(content,pageList);

    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<GundamListResponseDto> createPagedResponseDto(List<GundamListResponseDto> content, Page<Gundam> page) {
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


    /**
     * [조회]
     * 아이디 PK 값으로 조회
     * @param id 건담 아이디
     * @return Gundam 엔티티
     */
    public Gundam findById(Long id) {
        return gundamRepository.findById(id).orElseThrow(()->new GundamCustomException(ErrorMessage.NOT_FOUND_GUNDAM));
    }

    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        Pageable pageable = PageRequest.of(page, 20); // 페이지 0, 20개씩 보여줌
        return pageable;
    }


}
