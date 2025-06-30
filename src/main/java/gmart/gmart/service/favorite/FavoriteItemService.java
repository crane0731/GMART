package gmart.gmart.service.favorite;

import gmart.gmart.domain.FavoriteItem;
import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.dto.favorite.FavoriteItemListResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import gmart.gmart.repository.favorite.FavoriteItemRepository;
import gmart.gmart.service.item.ItemService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관심 상품 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteItemService {

    private final MemberService memberService;//회원 서비스
    private final ItemService itemService;//상품 서비스

    private final FavoriteItemRepository favoriteItemRepository;//관심 상품 레파지토리




    /**
     * [서비스 로직]
     * 현재 로그인한 회원이 상품을 관심 상품에 등록
     * @param itemId 상품 아이디
     */
    @Transactional
    public void createFavoriteItem(Long itemId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //관심 등록할 상품 조회
        Item item = itemService.findById(itemId);

        //관심 상품 생성 진행
        processSaveFavoriteItem(member, item);
    }

    /**
     * [서비스 로직]
     * 관삼 상품 존재 상태 반환
     * true : 존재
     * false : 없음
     * @param itemId 상품 아이디
     * @return boolean
     */
    public boolean getFavoriteItemStatus(Long itemId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //상품 조회
        Item item = itemService.findById(itemId);

        return favoriteItemRepository.existsByMemberAndItemAndDeleteStatus(member, item, DeleteStatus.UNDELETED);

    }


    /**
     * [서비스 로직]
     * 관심 상품 논리적 삭제 처리 (SOFT DELETE)
     * @param itemId 관심 상품 아이디
     */
    @Transactional
    public void deleteFavoriteItem(Long itemId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //상품 조회
        Item item = itemService.findById(itemId);

        //관심 상품 조회
        FavoriteItem favoriteItem = getFavoriteItem(member, item);

        //삭제할 관심 상품이 회원의 것인지 확인
        validateExistsFavoriteItemByMember(favoriteItem, member);

        //관심 상품 논리적 삭제 처리
        favoriteItem.softDelete();

    }

    /**
     * [서비스 로직]
     * 검색 조건에 따라 현재 로그인한 회원의 관심 상품 리스트 조회
     * @param itemTitle 상품 아이디
     * @return  List<FavoriteItemListResponseDto> 응답 DTO 리스트
     */
    public List<FavoriteItemListResponseDto> getAllFavoriteItems(String itemTitle){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        return favoriteItemRepository.findByCond(member,itemTitle).stream()
                .map(FavoriteItemListResponseDto::create)
                .toList();
    }



    /**
     * [저장]
     * @param favoriteItem 관심 상품 엔티티
     */
    public void save(FavoriteItem favoriteItem) {
        favoriteItemRepository.save(favoriteItem);
    }

    /**
     * [삭제]
     * @param favoriteItem
     */
    public void delete(FavoriteItem favoriteItem) {
        favoriteItemRepository.delete(favoriteItem);
    }

    /**
     * [조회]
     * ID(PK) 값으로 단일 조회
     * @param favoriteItemId 관심 상품 아이디
     * @return FavoriteItem 관심 상품 엔티티
     */
    public FavoriteItem findById(Long favoriteItemId) {
        return favoriteItemRepository.findById(favoriteItemId).orElseThrow(()->new ItemCustomException(ErrorMessage.NOT_FOUND_FAVORITE_ITEM));
    }



    /**
     * [조회]
     * 회원 + 상품 으로 조회
     * @param member 회원 아이디
     * @param item 상품 아이디
     * @return FavoriteItem 관심 상품 엔티티
     */
    public FavoriteItem findByMemberAndItem(Member member, Item item) {
        return favoriteItemRepository.findByMemberAndItem(member,item).orElseThrow(()->new ItemCustomException(ErrorMessage.NOT_FOUND_FAVORITE_ITEM));
    }

    //==회원이 이미 관심 등록한 상품인지 확인하고, Soft Delete 상태면 복구 처리==//
    private boolean  checkAndRestoreFavoriteItem(Member member, Item item) {

        FavoriteItem favoriteItem = favoriteItemRepository.findByMemberAndItem(member,item).orElse(null);

        if(favoriteItem!=null) {

            if (favoriteItem.getDeleteStatus().equals(DeleteStatus.UNDELETED)) {
                throw new ItemCustomException(ErrorMessage.ALREADY_FAVORITE_ITEM);

            }
            else{
                favoriteItem.recovery();
                return true; //복구했음
            }

        }
        return false; //복구 못함
    }

    //==삭제할 관심 상품이 회원의 것인지 확인하는 메서드==//
    private void validateExistsFavoriteItemByMember(FavoriteItem favoriteItem, Member member) {
        if(!favoriteItem.getMember().getId().equals(member.getId())) {
            throw new ItemCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==관심 상품 생성 로직 메서드 ==//
    private void processSaveFavoriteItem(Member member, Item item) {

        //회원이 이미 관심 등록한 상품인지 확인하고, Soft Delete 상태면 복구 처리
        if (checkAndRestoreFavoriteItem(member, item)) {
            return; // 복구만 했으면 여기서 종료 → 아래 save() 안함
        }

        //관심 상품 객체 생성
        FavoriteItem favoriteItem = FavoriteItem.create(member, item);

        //관심 상품 저장
        save(favoriteItem);

        //상품의 관심등록 수 증가
        item.plusFavoriteCount();
    }

    //==회원+상품으로 관심 상품 조회 로직==//
    private FavoriteItem getFavoriteItem(Member member, Item item) {
        FavoriteItem favoriteItem = favoriteItemRepository.findByMemberAndItem(member, item).orElseThrow(() -> new ItemCustomException(ErrorMessage.NOT_FOUND_FAVORITE_ITEM));
        return favoriteItem;
    }

}
