package gmart.gmart.dto.adminmessage;

import gmart.gmart.domain.AdminMessage;
import gmart.gmart.domain.enums.AdminMessageType;
import gmart.gmart.dto.enums.CreatedDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 메시지 검색조건 DTO
 */
@Getter
@Setter
public class SearchAdminMessageCondDto {

    private String content; //메시지 내용
    private AdminMessageType adminMessageType;//메시지 타입
    private CreatedDateSortType createdDateSortType; //최신순, 오래된 순 정렬


    /**
     * [생성 메서드]
     * @param content 메시지 내용
     * @param adminMessageType 메시지 타입
     * @param createdDateSortType 날짜 정렬 타입
     * @return SearchAdminMessageCondDto 검색 조건 DTO
     */
    public static SearchAdminMessageCondDto create(String content, AdminMessageType adminMessageType, CreatedDateSortType createdDateSortType) {
        SearchAdminMessageCondDto dto = new SearchAdminMessageCondDto();
        dto.setContent(content);
        dto.setAdminMessageType(adminMessageType);
        dto.setCreatedDateSortType(createdDateSortType);
        return dto;
    }
}
