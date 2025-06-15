package gmart.gmart.dto.adminmessage;

import gmart.gmart.domain.enums.AdminMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 메시지 등록 요청 DTO
 */
@Getter
@Setter
public class CreateAdminMessageRequestDto {

    @NotBlank(message = "메시지 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "메시지 타입을 입력해주세요")
    private AdminMessageType messageType;

}
