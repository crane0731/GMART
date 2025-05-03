package gmart.gmart.dto.api;

import lombok.Getter;
import lombok.Setter;

/**
 * API 응답 형식을 맞추기 위한 DTO
 */
@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;     // 성공 여부
    private String message;      // 메시지 (성공 or 오류)
    private T data;              // 실제 응답 데이터 (유형 제네릭)

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * api 요청에 성공했을때
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청이 성공했습니다.", data);
    }

    /**
     * api 요청에 실패했을때
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }



}
