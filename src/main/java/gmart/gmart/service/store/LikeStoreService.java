package gmart.gmart.service.store;

import gmart.gmart.domain.LikeStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import gmart.gmart.dto.store.LikeStoreListResponseDto;
import gmart.gmart.dto.store.SearchLikeStoreCondDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.StoreCustomException;
import gmart.gmart.repository.likestore.LikeStoreRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상점 좋아요 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeStoreService {

    private final MemberService memberService; //회원 서비스
    private final StoreService storeService; //상점 서비스

    private final LikeStoreRepository likeStoreRepository; //상점 좋아요 레파지토리

    /**
     * [서비스 로직]
     * 회원 상점 좋아요
     * @param storeId 상점 아이디
     */
    @Transactional
    public void likeStore(Long storeId) {

        //현재 로그인한 회원
        Member member = memberService.findBySecurityContextHolder();

        //상점 조회
        Store store = storeService.findById(storeId);

        //로그인한 회원이 이미 좋아요 한 상점인지 확인(이미 좋아요 한 상점이라면 예외를 던짐)
        existsLikeStore(member, store);

        //상점 좋아요 생성 + 저장
        like(store, member);
    }

    /**
     * [서비스 로직]
     * 회원 상점 좋아요 취소
     * @param storeId 상점 아이디
     */
    @Transactional
    public void cancelLikeStore(Long storeId) {

        //현재 로그인한 회원
        Member member = memberService.findBySecurityContextHolder();

        //상점 조회
        Store store = storeService.findById(storeId);

        //상점 좋아요 취소 로직
        cancelLike(member, store);

    }

    /**
     * [서비스 로직]
     * 검색 조건에 따라 현재 로그인한 회원의 좋아요한 상점 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<LikeStoreListResponseDto> 응답 DTO 리스트
     */
    public List<LikeStoreListResponseDto> findAllByCond(SearchLikeStoreCondDto condDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //상점 좋아요 리스트 조회 + 응답 DTO 리스트 생성 + 반환
        return likeStoreRepository.findAllByCond(member,condDto).stream()
                .map(LikeStoreListResponseDto::create)
                .toList();

    }





    /**
     * [조회]
     * 회원 + 상점 조합으로 상점 좋아요 조회
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return LikeStore 상점 좋아요 엔티티
     */
    public LikeStore findByMemberAndStore(Member member, Store store) {
        return likeStoreRepository.findByMemberAndStore(member, store).orElseThrow(() -> new StoreCustomException(ErrorMessage.NOT_FOUND_LIKE_STORE));
    }

    /**
     * [삭제]
     * @param likeStore 상점 좋아요 엔티티
     */
    @Transactional
    public void delete(LikeStore likeStore) {
        likeStoreRepository.delete(likeStore);
    }

    /**
     * [저장]
     * @param likeStore
     */
    @Transactional
    public void save(LikeStore likeStore) {
        likeStoreRepository.save(likeStore);
    }



    //==상점 좋아요 취소 로직(조회 + 좋아요 수 감소 +삭제)==//
    private void cancelLike(Member member, Store store) {
        //삭제 할 상점 좋아요 엔티티 조회
        LikeStore likeStore = findByMemberAndStore(member, store);

        //상점 좋아요 수 감소
        store.minusLikeCount();

        //논리적 삭제(SOFT DELETE)
        likeStore.softDelete();

    }

    //==로그인한 회원이 이미 좋아요 한 상점인지 확인 메서드 ==//
    private void existsLikeStore(Member member, Store store) {
        boolean exists = likeStoreRepository.existsByMemberAndStore(member, store);

        if (exists) {
            throw new StoreCustomException(ErrorMessage.ALREADY_LIKE_STORE);
        }
    }

    //==상점 좋아요 생성 + 저장==//
    private void like(Store store, Member member) {
        //상점 좋아요 수 늘리기
        store.plusLikedCount();

        //상점 좋아요 객체 생성
        LikeStore likeStore = LikeStore.create(member, store);

        //저장
        save(likeStore);
    }







}
