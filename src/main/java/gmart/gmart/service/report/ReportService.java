package gmart.gmart.service.report;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Report;
import gmart.gmart.domain.enums.ReporterRole;
import gmart.gmart.dto.report.CreateReportRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import gmart.gmart.exception.ReportCustomException;
import gmart.gmart.repository.report.ReportRepository;
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
public class ReportService {

    private final MemberService memberService; //회원 서비스
    private final ItemService itemService; //상품 서비스

    private final ReportRepository reportRepository; //상품 신고 레파지토리


    /**
     * [서비스 로직]
     * 상품 신고
     * @param requestDto 상품 신고 요청 DTO
     */
    @Transactional
    public void report(CreateReportRequestDto requestDto) {

        //현재 로그인한 회원 조회(신고자)
        Member reporter = memberService.findBySecurityContextHolder();

        //신고할 상품 조회
        Item item = itemService.findById(requestDto.getItemId());

        //신고 당할 회원 조회
        Member reportedMember = memberService.findById(requestDto.getReportedMemberId());

        //검증 로직
        validateLogic(requestDto, reporter, reportedMember, item);

        //상품 신고 로직 진행
        processReport(requestDto, reporter, reportedMember, item);

    }

    /**
     * [저장]
     * @param report 상품 신고 엔티티
     */
    @Transactional
    public void save(Report report) {
        reportRepository.save(report);
    }


    /**
     * [삭제]
     * @param report 상품 신고 엔티티
     */
    @Transactional
    public void delete(Report report) {
        reportRepository.delete(report);
    }

    /**
     * [조회]
     * ID (PK) 값으로 단일 조회
     * @param id 신고 상품 아이디
     * @return ReportItem 신고 상품 엔티티
     */
    public Report findById(Long id) {
        return reportRepository.findById(id).orElseThrow(()-> new ItemCustomException(ErrorMessage.NOT_FOUND_REPORT));
    }

    //==검증 로직==//
    private void validateLogic(CreateReportRequestDto requestDto, Member reporter, Member reportedMember, Item item) {
        //만약 이미 같은 신고 내용이 있는지 확인
        validateExistsReport(requestDto, reporter, reportedMember, item);

        //신고자와 피신고자가 같은지 확인
        validateReporterEqualsReportedMember(reporter, reportedMember);

        //만약 신고자가 구매자 라면
        if(requestDto.getReporterRole().equals(ReporterRole.BUYER)){
            //신고할 상품이 피신고자의 상품인지 확인
            validateItemOwner(item, reportedMember);
        }
    }


    //==상품 신고 로직==//
    private void processReport(CreateReportRequestDto requestDto, Member reporter, Member reportedMember, Item item) {
        //상품 신고 객체 생성
        Report report = Report.create(requestDto.getReporterRole(),reporter, reportedMember, item, requestDto.getReason());

        //상품 신고 저장
        save(report);

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


    //==신고할 상품이 피신고자의 상품인지 확인하는 로직==//
    private void validateItemOwner(Item item, Member reportedMember) {
        if(item.getStore().getMember().getId()!= reportedMember.getId()){
            throw new ItemCustomException(ErrorMessage.NOT_ITEM_SELLER);
        }
    }

    //==만약 이미 같은 신고 내용이 있는지 확인 하는 로직==//
    private void validateExistsReport(CreateReportRequestDto requestDto, Member reporter, Member reportedMember, Item item) {
        Boolean exists = reportRepository.existsByReporterAndReportedMemberAndItemAndReporterRole(reporter, reportedMember, item, requestDto.getReporterRole());
        if(exists) {
            throw new ReportCustomException(ErrorMessage.ALREADY_REPORT);
        }
    }



}
