package gmart.gmart.controller.gundam;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import gmart.gmart.service.gundam.GundamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 건담 정보 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/gundam")
public class GundamController {

    private final GundamService gundamService; //건담 정보 서비스

    /**
     * [컨트롤러]
     * 조건에 따라 건담 정보 리스트를 조회
     * @param condDto 검색 조건
     * @return List<GundamListResponseDto> 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> findAllByCond(@RequestBody SearchGundamCondDto condDto){

        List<GundamListResponseDto> responseDto = gundamService.findAllByCond(condDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }
}
