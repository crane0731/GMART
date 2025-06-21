package gmart.gmart.dto.inquiry;

import gmart.gmart.domain.enums.AnswerStatus;
import gmart.gmart.dto.enums.CreatedDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 문의리스트를 조회하기위한 조건을 담은 DTO
 */
@Getter
@Setter
public class SearchInquiryCondDto {

    private String title; //문의 제목

    private AnswerStatus answerStatus; //  답변 상태 -> [UNANSWERED,ANSWERED]

    private CreatedDateSortType createdDateSortType; //날짜 정렬 타입


    /**
     * [생성 메서드]
     * @param title 문의 제목
     * @param answerStatus 답변 상태
     * @param createdDateSortType 날짜 정렬 타입
     * @return SearchInquiryCondDto 검색 조건 DTO
     */
    public static SearchInquiryCondDto create(String title,AnswerStatus answerStatus, CreatedDateSortType createdDateSortType){

        SearchInquiryCondDto searchInquiryCondDto= new SearchInquiryCondDto();

        searchInquiryCondDto.setTitle(title);
        searchInquiryCondDto.setAnswerStatus(answerStatus);
        searchInquiryCondDto.setCreatedDateSortType(createdDateSortType);

        return searchInquiryCondDto;
    }

}
