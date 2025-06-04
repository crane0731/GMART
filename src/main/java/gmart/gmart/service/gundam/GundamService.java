package gmart.gmart.service.gundam;

import gmart.gmart.domain.Gundam;
import gmart.gmart.dto.gundam.GundamListResponseDto;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.GundamCustomException;
import gmart.gmart.repository.gundam.GundamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  건담 정보 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GundamService {

    private final GundamRepository gundamRepository; //건담 정보 레파지토리


    /**
     * 건담 정보 리스트 조회
     */
    public List<GundamListResponseDto> findAllByCond(SearchGundamCondDto condDto){
        return gundamRepository.findAllByCond(condDto)
                .stream()
                .map(GundamListResponseDto::createDto)
                .toList();
    }


    /**
     * [조회]
     * 아이디 PK 값으로 조회
     * @param id 건담 아이디
     * @return Gundam 엔티티
     */
    public Gundam findById(Long id) {
        return gundamRepository.findById(id).orElseThrow(()->new GundamCustomException(ErrorMessage.NOT_FOUND_GUNDAM));
    }


}
