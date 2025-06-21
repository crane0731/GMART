package gmart.gmart.service.admin;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.dto.inquiry.InquiryAnswerRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.InquiryCustomException;
import gmart.gmart.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 문의 관리 서비스
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminInquiryService {

    private final InquiryService inquiryService;//문의 서비스

    /**
     * 관리자 문의 삭제 서비스
     * @param inquiryId 문의 아이디
     */
    @Transactional
    public void deleteInquiry(Long inquiryId){

        //문의 조회
        Inquiry inquiry = inquiryService.findById(inquiryId);

        //문의 삭제
        inquiry.softDelete();
    }

    /**
     * 관리자 문의 답변 등록
     * @param inquiryId 문의 아이디
     * @param requestDto 답변 요청 DTO
     */
    @Transactional
    public void createAnswer(Long inquiryId, InquiryAnswerRequestDto requestDto){

        //문의 조회
        Inquiry inquiry = inquiryService.findById(inquiryId);

        //답변이 이미 등록되어있는지 확인
        //이미 등록되어 있다면 예외를 던짐
        validateAnswerNotNull(inquiry);

        //답변 등록
        inquiry.creteAnswer(requestDto.getAnswer());
    }

    /**
     * 관리자 문의 답변 삭제
     * @param inquiryId 문의 아이디(PK)
     */
    @Transactional
    public void deleteAnswer(Long inquiryId){

        //문의 조회
        Inquiry inquiry = inquiryService.findById(inquiryId);

        //답변이 없는지 확인
        //삭제할 답변이 없다면 예외를 던짐
        validateAnswerNull(inquiry);

        //답변 삭제
        inquiry.deleteAnswer();
    }



    //==삭제할 답변이 없는지 확인하는 로직==//
    private void validateAnswerNull(Inquiry inquiry) {
        if(inquiry.getAnswer() ==null){
            throw new InquiryCustomException(ErrorMessage.NOT_FOUND_ANSWER);

        }
    }

    //==답변이 이미 등록되어있는지 확인하는 로직==//
    private void validateAnswerNotNull(Inquiry inquiry) {
        if(inquiry.getAnswer() !=null){
            throw new InquiryCustomException(ErrorMessage.ALREADY_ANSWER);
        }
    }

}
