package gmart.gmart.service.favorite;

import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.favorite.FavoriteGundamListResponseDto;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;
import gmart.gmart.exception.CustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.favorite.FavoriteGundamRepository;
import gmart.gmart.service.gundam.GundamService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 관심 건담 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteGundamService {

    private final MemberService memberService; //회원 서비스
    private final GundamService gundamService; //건담 서비스

    private final FavoriteGundamRepository favoriteGundamRepository; //회원 관심 건담 레파지토리


    /**
     * [서비스 로직]
     * 회원 관심 건담 등록
     * @param gundamId 건담 아이디
     */
    @Transactional
    public void createFavoriteGundam(Long gundamId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //건담 조회
        Gundam gundam = gundamService.findById(gundamId);

        //회원이 이미 해당 건담을 관심 건담으로 등록했는지 확인
        validateNotAlreadyFavoritedGundam(member, gundam);

        //관심 건담 객체 생성
        FavoriteGundam favoriteGundam = FavoriteGundam.create(member, gundam);

        //저장
        save(favoriteGundam);

    }

    /**
     * [서비스 로직]
     * 회원 관심 건담 삭제
     * @param favoriteGundamId 관심 건담 아이디
     */
    @Transactional
    public void deleteFavoriteGundam(Long favoriteGundamId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //관심 건담 조회
        FavoriteGundam favoriteGundam = findById(favoriteGundamId);

        //해당 관심건담 이 회원의 것인지 확인
        validateFavoriteGundamOwner(member, favoriteGundam);

        //관심 건담 삭제
        delete(favoriteGundam);

    }


    /**
     * [서비스 로직]
     * 검색 조건에 따라 로그인한 회원의 관심 건담 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<FavoriteGundamListResponseDto> 응답 DTO 리스트
     */
    public List<FavoriteGundamListResponseDto> findAllByCond(SearchFavoriteGundamCondDto condDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //로그인한 회원 + 검색 조건으로 리스트 조회 후 DTO 로 변경
        return findFavoritesByCond(condDto, member);
    }

    /**
     * [저장]
     * @param favoriteGundam 관심 건담 엔티티
     */
    @Transactional
    public void save(FavoriteGundam favoriteGundam) {
        favoriteGundamRepository.save(favoriteGundam);
    }

    /**
     * [삭제]
     * @param favoriteGundam 관심 건담 엔티티
     */
    @Transactional
    public void delete(FavoriteGundam favoriteGundam) {
        favoriteGundamRepository.delete(favoriteGundam);
    }

    /**
     * [조회]
     * 아이디 PK 값으로 단일 조회
     * @param id 관심건담 아이디
     * @return FavoriteGundam 관심 건담 엔티티
     */
    public FavoriteGundam findById(Long id) {
        return favoriteGundamRepository.findById(id).orElseThrow(()->new CustomException(ErrorMessage.NOT_FOUND_FAVORITE_GUNDAM));
    }

    //관심건담 이 회원의 것인지 확인하는 메서드//
    private void validateFavoriteGundamOwner(Member member, FavoriteGundam favoriteGundam) {
        if(!member.getId().equals(favoriteGundam.getMember().getId())){
            throw new CustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==회원이 이미 해당 건담을 관심 건담으로 등록했는지 확인하는 메서드==//
    private void validateNotAlreadyFavoritedGundam(Member member, Gundam gundam) {

        if(favoriteGundamRepository.existsByMemberAndGundam(member, gundam)) {
            throw new CustomException(ErrorMessage.ALREADY_FAVORITE_GUNDAM);
        }
    }

    //==로그인한 회원 + 검색 조건으로 리스트 조회 후 DTO 로 변경하는 메서드==//
    private List<FavoriteGundamListResponseDto> findFavoritesByCond(SearchFavoriteGundamCondDto condDto, Member member) {
        return favoriteGundamRepository.findAllByMemberAndCond(condDto, member)
                .stream()
                .map(FavoriteGundamListResponseDto::create)
                .toList();
    }

}
