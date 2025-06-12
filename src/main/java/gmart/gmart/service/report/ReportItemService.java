package gmart.gmart.service.report;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.ReportItem;
import gmart.gmart.dto.reportitem.CreateReportItemRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import gmart.gmart.repository.reportitem.ReportItemRepository;
import gmart.gmart.service.item.ItemService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 신고 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportItemService {

    private final MemberService memberService; //회원 서비스
    private final ItemService itemService; //상품 서비스

    private final ReportItemRepository reportItemRepository; //상품 신고 레파지토리


    /**
     * [서비스 로직]
     * 상품 신고
     * @param itemId 상품 아이디
     * @param requestDto 상품 신고 요청 DTO
     */
    @Transactional
    public void report(Long itemId , CreateReportItemRequestDto requestDto) {

        //현재 로그인한 회원 조회(신고자)
        Member reporter = memberService.findBySecurityContextHolder();

        //신고할 상품 조회
        Item item = itemService.findById(itemId);

        //신고 당할 회원 조회
        Member reportedMember = item.getStore().getMember();

        //신고자와 피신고자가 같은지 확인
        validateReporterEqualsReportedMember(reporter, reportedMember);

        //상품 신고 로직 진행
        processReportItem(requestDto, reporter, reportedMember, item);

    }

    //==상품 신고 로직==//
    private void processReportItem(CreateReportItemRequestDto requestDto, Member reporter, Member reportedMember, Item item) {
        //상품 신고 객체 생성
        ReportItem reportItem = ReportItem.create(reporter, reportedMember, item, requestDto.getReason());

        //상품 신고 저장
        save(reportItem);

        //상품 신고 처리
        item.reported();

        //신고당한 회원의 신고 받은 수 증가
        reportedMember.plusReportedCount();
    }

    //==신고자와 피신고자가 같은지 확인 하는 메서드==//
    private void validateReporterEqualsReportedMember(Member reporter, Member reportedMember) {
        if(reporter.getId().equals(reportedMember.getId())) {
            throw new ItemCustomException(ErrorMessage.SELF_REPORT_NOT_ALLOWED);
        }
    }


    /**
     * [저장]
     * @param reportItem 상품 신고 엔티티
     */
    @Transactional
    public void save(ReportItem reportItem) {
        reportItemRepository.save(reportItem);
    }


    /**
     * [삭제]
     * @param reportItem 상품 신고 엔티티
     */
    @Transactional
    public void delete(ReportItem reportItem) {
        reportItemRepository.delete(reportItem);
    }

    /**
     * [조회]
     * ID (PK) 값으로 단일 조회
     * @param id 신고 상품 아이디
     * @return ReportItem 신고 상품 엔티티
     */
    public ReportItem findById(Long id) {
        return reportItemRepository.findById(id).orElseThrow(()-> new ItemCustomException(ErrorMessage.NOT_FOUND_REPORT_ITEM));
    }


}
