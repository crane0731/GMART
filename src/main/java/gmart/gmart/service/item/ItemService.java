package gmart.gmart.service.item;

import gmart.gmart.command.CommandMapper;
import gmart.gmart.domain.*;
import gmart.gmart.dto.inquiry.InquiryListResponseDto;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;
import gmart.gmart.dto.item.*;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ItemCustomException;
import gmart.gmart.repository.item.ItemRepository;
import gmart.gmart.service.gundam.GundamService;
import gmart.gmart.service.image.UploadItemImageService;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * [서비스 로직]
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

        //상품 저장 로직
        processSaveItem(requestDto, store);
    }


    /**
     * [서비스 로직]
     * 상품 논리적 삭제 처리 (SOFT DELETE)
     * 실제로 상품 엔티티를 삭제하는 것이 아닌 상품 활성화 상태만 비활성으로 변경 처리함
     * @param itemId 상품 아이디
     */
    @Transactional
    public void softDelete(Long itemId) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //회원의 상점 조회
        Store store = member.getStore();

        //삭제할 상품 조회
        Item item = findById(itemId);

        //삭제 할 상품이 현재 로그인한 회원이 등록한 상품인지 확인(아니라면 예외를 던짐)
        validateItemOwner(item, member);

        //상품 삭제 로직
        processDeleteItem(item, store);
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

        //수정 할 상품이 회원의 것인지 확인(아닐 경우 예외를 던짐)
        validateItemOwner(item, member);

        //기존의 상품 이미지와 상품 건담 제거
        removeOldItemImageAndItemGundam(item);

        //모두 업데이트(with 새로운 상품이미지, 새로운 상품 건담)
        processUpdateAll(requestDto, item);
    }

    /**
     * [서비스 로직]
     * 상품 정보 상세 조회
     * @param itemId 상품 아이디
     * @return ItemDetailsResponseDto 응답 DTO
     */
    @Transactional
    public ItemDetailsResponseDto getItemDetails(Long itemId) {

        //상품 엔티티 조회
        Item item = findById(itemId);

        item.plusViewCount();

        //상품 상세 조회 응답 DTO 생성 + 반환
        return ItemDetailsResponseDto.create(item);
    }

    /**
     * [서비스 로직]
     * 검색 조건에 따라 상품 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<ItemListResponseDto> 응답 DTO 리스트
     */
    public PagedResponseDto<ItemListResponseDto> getAllItems(SearchItemCondDto condDto, int page) {

        Page<Item> pageList = itemRepository.findAllByCond(condDto, createPageable(page));

        List<ItemListResponseDto> content = pageList.getContent().stream().map(ItemListResponseDto::create).toList();

        return createPagedResponseDto(content,pageList);

    }

    /**
     * [서비스 로직]
     * 검색 조건에 따라 특정상점의의 상품 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<ItemListResponseDto> 응답 DTO 리스트
     */
    public PagedResponseDto<ItemListResponseDto> getStoreItems(Long storeId,SearchItemCondDto condDto, int page){

        //상점 조회
        Store store = storeService.findById(storeId);

        Page<Item> pageList = itemRepository.findAllByCondAndStore(store, condDto, createPageable(page));

        List<ItemListResponseDto> content = pageList.getContent().stream().map(ItemListResponseDto::create).toList();

        return createPagedResponseDto(content,pageList);
    }


    /**
     * [서비스 로직]
     * 검색 조건에 따라 자신의 상품 리스트 조회
     * @param condDto 검색 조건 DTO
     * @return List<ItemListResponseDto> 응답 DTO 리스트
     */
    public PagedResponseDto<ItemListResponseDto> getAllMyItems(SearchItemCondDto condDto, int page) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        Page<Item> pageList = itemRepository.findAllByCondAndMember(member,condDto, createPageable(page));

        List<ItemListResponseDto> content = pageList.getContent().stream().map(ItemListResponseDto::create).toList();

        return createPagedResponseDto(content,pageList);

    }

    /**
     * [서비스 로직]
     * 로그인한 회원의 관심 건담에 해당되는 상품목록을 조회
     * @param page 페이지 번호
     * @return PagedResponseDto<ItemListResponseDto>
     */
    public PagedResponseDto<ItemListResponseDto> getItemsByMemberFavoriteGundams(int page) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //회원 관심 건담 -> 건담 리스트 조회
        List<Gundam> gundams = member.getFavoriteGundams().stream().map(FavoriteGundam::getGundam)
                .toList();

        Page<Item> pageList = itemRepository.findAllByGundams(gundams, createPageable(page));

        List<ItemListResponseDto> content = pageList.getContent().stream().map(ItemListResponseDto::create).toList();

        return createPagedResponseDto(content,pageList);
    }


    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<ItemListResponseDto> createPagedResponseDto(List<ItemListResponseDto> content, Page<Item> page) {
        return new PagedResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }

    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        // 페이지 0, 10개씩 보여줌
        return PageRequest.of(page, 10);
    }



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

    //==기존의 상품 이미지와 상품 건담 제거 메서드==//
    private void removeOldItemImageAndItemGundam(Item item) {
        //기존 상품 업로드 이미지 비 사용 처리
        usedFalseUploadImage(item);

        //기존 상품 이미지 전부 삭제
        item.removeAllItemImages();

        //기존 상품 건담 전부 삭제
        item.removeAllItemGundams();
    }

    //업데이트 로직 (with 새로운 상품이미지, 새로운 상품 건담) 메서드//
    private void processUpdateAll(UpdateItemRequestDto requestDto, Item item) {
        //새로운 상품 업로드 이미지 사용 처리
        addItemImages(requestDto.getItemImages(), item);

        //새로운 상품 건담 생성
        addItemGundam(requestDto.getGundamList(), item);

        //필드 값 업데이트
        item.update(CommandMapper.updateItemCommand(requestDto));
    }

    //==상품이 회원의 것인지 확인하는 메서드==//
    private void validateItemOwner(Item item, Member member) {
        if(!item.getStore().getMember().getId().equals(member.getId())){
            throw new ItemCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==상품 저장 로직 메서드==//
    private void processSaveItem(CreateItemRequestDto requestDto, Store store) {
        //상품 엔티티 생성
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

    //==상품 삭제 로직 메서드==//
    private void processDeleteItem(Item item, Store store) {

        //상품 업로드 이미지 비 사용 처리
        //usedFalseUploadImage(item);

        //상품 삭제(비활성화 처리)
        item.softDelete();

        //상점의 상품수 감소
        store.minusItemCount();
    }
}
