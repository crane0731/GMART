package gmart.gmart.service.admin;

import gmart.gmart.domain.Report;
import gmart.gmart.dto.enums.AdminReportAcceptStatus;
import gmart.gmart.dto.report.AdminAcceptReportRequestDto;
import gmart.gmart.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 - 신고 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final ReportService reportService; //신고 서비스


    /**
     * [서비스 로직]
     * 관리자가 신고를 처리
     * 수락 / 거절
     * @param reportId 신고 아이디
     * @param requestDto 요청 DTO
     */
    @Transactional
    public void changeReportStatus(Long reportId,AdminAcceptReportRequestDto requestDto){

        //수락 처리할 신고
        Report report = reportService.findById(reportId);

        if(requestDto.getStatus()==AdminReportAcceptStatus.ACCEPT){
            report.accept();
        }else {
            report.reject();
        }
    }


    /**
     * 관리자가 신고를 상세 조회
     */

    /**
     * 관리자가 신고를 리스트로 조회
     */


}
