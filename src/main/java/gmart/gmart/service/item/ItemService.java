package gmart.gmart.service.item;

import gmart.gmart.command.CommandMapper;
import gmart.gmart.domain.*;
import gmart.gmart.dto.item.ChangeSaleStatusRequestDto;
import gmart.gmart.dto.item.CreateItemRequestDto;
import gmart.gmart.dto.item.ItemImageRequestDto;
import gmart.gmart.dto.item.UpdateItemRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import gmart.gmart.repository.item.ItemRepository;
import gmart.gmart.service.gundam.GundamService;
import gmart.gmart.service.image.UploadItemImageService;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상품 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final MemberService memberService; //회원 서비스
    private final StoreService storeService;// 상점 서비스
    private final UploadItemImageService uploadItemImageService;//상점 이미지 서비스
    private final GundamService gundamService; //건담 서비스

    private final ItemRepository itemRepository;//상품 레파지토리


    /**
     * [비즈니스 로직]
     * 상품 판매 상태 변경
     * @param itemId 상품 아이디
     * @param requestDto 판매 상태 변경 요청 DTO
     */
    @Transactional
    public void changeSaleStatus(Long itemId, ChangeSaleStatusRequestDto requestDto ) {
        //상품 조회
        Item item = findById(itemId);

        //판매 완료
        item.changeSaleStatus(requestDto.getSaleStatus());

    }

    /**
     * [서비스 로직]
     * @param requestDto 상품 생성 요청 DTO
     */
    @Transactional
    public void createItem( CreateItemRequestDto requestDto) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //상점 조회
        Store store = member.getStore();

        //상품을 등록하려는 상점이 현재 로그인한 회원의 상점인지 확인
        storeService.validateStoreOwner(store, member);

        //상품 생성
        Item item = Item.create(store, CommandMapper.createItemCommand(requestDto));

        //상품 이미지를 생성하고 상품에 추가
        addItemImages(requestDto.getItemImages(), item);

        //상품 건담을 생성하고 상품에 추가
        addItemGundam(requestDto.getGundamList(), item);

        //상품 저장
        save(item);

        //상점의 상품수 증가
        store.plusItemCount();
    }


    /**
     * [서비스 로직]
     * 상품 삭제
     * @param itemId 상품 아이디
     */
    @Transactional
    public void deleteItem(Long itemId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //회원의 상점 조회
        Store store = member.getStore();

        //삭제할 상품 조회
        Item item = findById(itemId);

        //상품 업로드 이미지 비 사용 처리
        usedFalseUploadImage(item);

        //상품 삭제
        delete(item);

        //상점의 상품수 감소
        store.minusItemCount();
    }


    /**
     * [서비스 로직]
     * 상품 업데이트
     * @param itemId 상품 아이디
     * @param requestDto 상품 업데이트 요청 DTO
     */
    @Transactional
    public void updateItem(Long itemId, UpdateItemRequestDto requestDto){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //수정 할 상품 조회
        Item item = findById(itemId);

        //기존 상품 업로드 이미지 비 사용 처리
        usedFalseUploadImage(item);

        //기존 상품 이미지 전부 삭제
        item.removeAllItemImages();

        //새로운 상품 업로드 이미지 사용 처리
        addItemImages(requestDto.getItemImages(), item);

        //기존 상품 건담 전부 삭제
        item.removeAllItemGundams();

        //새로운 상품 건담 생성
        addItemGundam(requestDto.getGundamList(), item);

        //필드 값 업데이트
        item.update(CommandMapper.updateItemCommand(requestDto));
    }



    /**
     * 상품 조회
     */

    /**
     * 상품 리스트 조회
     */



    /**
     * [저장]
     * @param item 상품 엔티티
     */
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    /**
     * [삭제]
     * @param item 상품 엔티티
     */
    @Transactional
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    /**
     * [조회]
     * 아이디 PK 값으로 조회
     * @param id 상점 아이디
     * @return Item 상점 엔티티
     */
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(()->new ItemCustomException(ErrorMessage.NOT_FOUND_ITEM));
    }


    //==상품 이미지를 생성하고 상품에 추가하는  메서드==//
    private void addItemImages(List<ItemImageRequestDto> itemImages, Item item) {
        if(itemImages !=null) {
            for (ItemImageRequestDto image : itemImages) {
                //업로드된 이미지 조회
                UploadedImage uploadedImage = uploadItemImageService.findByImageUrl(image.getImageUrl());

                //업로드 이미지 사용 처리
                uploadedImage.usedTrue();

                //상품 이미지 생성
                ItemImage.create(item, image.getImageUrl(), image.getIsMain());
            }
        }
    }

    //==상품 건담을 생성하고 상품에 추가 하는 메서드==//
    private void addItemGundam(List<Long> gundamList, Item item) {
        if(gundamList !=null) {
            for (Long gundamId : gundamList) {

                //건담 정보 조회
                Gundam gundam = gundamService.findById(gundamId);

                //상품 건담 생성
                ItemGundam.create(item, gundam);
            }
        }
    }

    //==상품 업로드 이미지 비사용 처리 메서드==//
    private void usedFalseUploadImage(Item item) {
        List<ItemImage> itemImages = item.getItemImages();
        for (ItemImage itemImage : itemImages) {

            UploadedImage uploadedImage = uploadItemImageService.findByImageUrl(itemImage.getImageUrl());

            //업로드 이미지 비사용 처리
            uploadedImage.usedFalse();
        }
    }
}
