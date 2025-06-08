package gmart.gmart.service.favorite;

import gmart.gmart.domain.FavoriteStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import gmart.gmart.dto.favorite.FavoriteStoreListResponseDto;
import gmart.gmart.dto.favorite.SearchFavoriteStoreCondDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.StoreCustomException;
import gmart.gmart.repository.favorite.FavoriteStoreRepository;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관심 상점 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteStoreService {

    private final MemberService memberService;//회원 서비스
    private final StoreService storeService;//상점 서비스

    private final FavoriteStoreRepository favoriteStoreRepository; //관심 상점 레파지토리


    /**
     * [서비스 로직]
     * 관심 상점 등록
     * @param storeId 상점 아이디
     */
    @Transactional
    public void addFavoriteStore(Long storeId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //관심 등록할 상점 조회
        Store store = storeService.findById(storeId);

        //현재 로그인한 회원이 이미 관심 등록한 상점인지 확인(이미 등록한 상점이라면 예외를 던짐)
        existsFavoriteStore(member, store);


        //관심 상점에 등록
        saveFavoriteStore(member, store);

    }

    /**
     * [서비스 로직]
     * 관심 상점 삭제
     * @param storeId 상점 아이디
     */
    @Transactional
    public void deleteFavoriteStore(Long storeId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //관심 삭제할 상점 조회
        Store store = storeService.findById(storeId);

        //관심 상점 삭제 로직
        processDelete(member, store);

    }

    /**
     * [서비스 로직]
     * 현재 로그인한 회원의 관심 상점 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return  List<FavoriteStoreListResponseDto> 응답 DTO 리스트
     */
    public List<FavoriteStoreListResponseDto> getFavoriteStoreList(SearchFavoriteStoreCondDto condDto) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //관심 상점 조회 + 응답 DTO 리스트 생성 + 반환
        return favoriteStoreRepository.findAllByCond(member,condDto).stream()
                .map(FavoriteStoreListResponseDto::create)
                .toList();
    }



    /**
     * [조회]
     * 회원 + 상점 으로 조회
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return  FavoriteStore 관심상점 엔티티
     */
    public FavoriteStore findByMemberAndStore(Member member, Store store) {
        return favoriteStoreRepository.findByMemberAndStore(member, store).orElseThrow(() -> new StoreCustomException(ErrorMessage.NOT_FOUND_FAVORITE_STORE));
    }

    /**
     * [저장]
     * @param favoriteStore 관심 상점 엔티티
     */
    @Transactional
    public void save(FavoriteStore favoriteStore) {
        favoriteStoreRepository.save(favoriteStore);
    }

    /**
     * [삭제]
     * @param favoriteStore 관심 상점 엔티티
     */
    @Transactional
    public void delete(FavoriteStore favoriteStore) {
        favoriteStoreRepository.delete(favoriteStore);
    }

    //==관심 상점을 생성하고 저장하는 메서드==//
    private void saveFavoriteStore(Member member, Store store) {
        //관심 상점 객체 생성
        FavoriteStore favoriteStore = FavoriteStore.create(member, store);

        //상점의 관심 수 증가
        store.plusFavoriteCount();

        //관심 상점 저장
        save(favoriteStore);
    }

    //==현재 로그인한 회원이 이미 관심 등록한 상점인지 확인하는 메서드==//
    private void existsFavoriteStore(Member member, Store store) {
        Boolean exists = favoriteStoreRepository.existsByMemberAndStore(member, store);
        if (exists) {
            throw new StoreCustomException(ErrorMessage.ALREADY_FAVORITE_STORE);
        }
    }

    //==관심 상점 삭제 로직 메서드==//
    private void processDelete(Member member, Store store) {
        //삭제할 관심 상점 엔티티 조회
        FavoriteStore favoriteStore = findByMemberAndStore(member, store);

        //상점의 관심상점 등록수 감소
        store.minusFavoriteCount();

        //삭제
        delete(favoriteStore);
    }
}
