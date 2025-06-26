package gmart.gmart.controller.gundam;

import gmart.gmart.domain.enums.GundamGrade;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.gundam.GundamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param name 건담 이름
     * @param grade 건담 등급
     * @param page 페이지 번호
     * @return PagedResponseDto<GundamListResponseDto> 페이징된 응답 DTO 리스트
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> findAllByCond(@RequestParam(value = "name" ,required = false)String name,
                                                        @RequestParam(value = "grade", required = false)GundamGrade grade,
                                                        @RequestParam(value = "page", defaultValue = "0") int page
                                                        ) {
        SearchGundamCondDto condDto = SearchGundamCondDto.create(name, grade);

        PagedResponseDto<GundamListResponseDto> responseDtos = gundamService.findAllByCond(condDto, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtos));
    }
}
