package gmart.gmart.service.store;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import gmart.gmart.domain.StoreProfileImage;
import gmart.gmart.domain.UploadedImage;
import gmart.gmart.dto.store.*;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.StoreCustomException;
import gmart.gmart.repository.store.StoreRepository;
import gmart.gmart.service.image.UploadStoreProfileImageService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상점 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {


    private final MemberService memberService; //회원 서비스
    private final UploadStoreProfileImageService uploadStoreProfileImageService;//업로드 상점 이미지 서비스


    private final StoreRepository storeRepository; //상점 레파지토리

//    /**
//     * [서비스 로직]
//     * 상점 생성
//     * @param requestDto 상점 생성 요청 DTO
//     */
//    @Transactional
//    public void createStore(CreateStoreRequestDto requestDto) {
//
//        //현재 로그인한 회원 조회
//        Member member = memberService.findBySecurityContextHolder();
//
//        //업로드 이미지 조회(만약 프로필 이미지가 null 이면 기본 상점 이미지 조회)
//        UploadedImage uploadedImage = getUploadedImage(requestDto);
//
//        //상점 등록 + 상점 프로필 이미지 등록
//        processSaveStoreWithProfileImage(member,requestDto, uploadedImage);
//
//    }

    /**
     * [서비스 로직]
     * 상점 업데이트
     * @param storeId 상점 아이디
     * @param requestDto 상점 수정 요청 DTO
     */
    @Transactional
    public void updateStore(Long storeId,UpdateStoreRequestDto requestDto) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //수정할 상점 조회
        Store store = findById(storeId);

        //수정할 상점이 로그인한 회원의 상점인지 확인(회원의 것이 아니면 예외를 던짐)
        validateStoreOwner(store, member);

        //상점 업데이트 실행
        processUpdate(requestDto, store);
    }


    /**
     * [서비스 로직]
     * 상점 정보 상세 조회
     * @param storeId 상점 아이디
     * @return StoreResponseDto 응답 DTO
     */
    @Transactional
    public StoreResponseDto getStoreDetails(Long storeId) {
        //상점 엔티티 조회
        Store store = findById(storeId);

        //상점 총 방문수 증가
        store.plusVisitedCount();

        //상점 응답 DTO 생성 + 반환
        return StoreResponseDto.create(store);
    }


    /**
     * [서비스 로직]
     * 자신의 상점 정보 조회
     * @return StoreResponseDto 응답 DTO
     */
    @Transactional
    public StoreResponseDto getMyStoreDetails(){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //회원의 상점 조회
        Store store = member.getStore();

        //상점 응답 DTO 생성 + 반환
        return StoreResponseDto.create(store);
    }


    /**
     * [서비스 로직]
     * 조건에 따라 상점 리스트 검색(상위 10개)
     * @param condDto 검색 조건 DTO
     * @return List<StoreListResponseDto> 응답 DTO 리스트
     */
    public List<StoreListResponseDto> findAllByCond(SearchStoreCondDto condDto){
        return storeRepository.findAllByCond(condDto).stream()
                .map(StoreListResponseDto::create)
                .toList();
    }



    /**
     * [저장]
     * @param store 상점 엔티티
      */
    @Transactional
    public void save(Store store) {
        storeRepository.save(store);
    }

    /**
     * [삭제]
     * @param store 상점 엔티티
     */
    @Transactional
    public void delete(Store store) {
        storeRepository.delete(store);
    }

    /**
     * [조회]
     * ID(PK)값으로 단일 조회
     * @param id 상점 아이디
     * @return Store 상점 엔티티
     */
    public Store findById(Long id) {
        return storeRepository.findById(id).orElseThrow(()->new StoreCustomException(ErrorMessage.NOT_FOUND_STORE));
    }

//    //==상점 등록 + 상점 프로필 이미지 등록 메서드==//
//    private void processSaveStoreWithProfileImage(Member member,CreateStoreRequestDto requestDto, UploadedImage uploadedImage) {
//        //상점 프로필 이미지 객체 생성
//        StoreProfileImage storeProfileImage = StoreProfileImage.create(uploadedImage.getImageUrl());
//
//        //상점 객체 생성 + 얀관관계 세팅
//        Store store = Store.create(member,requestDto.getName(), requestDto.getIntroduction(), storeProfileImage);
//
//        //상점 저장
//        save(store);
//    }

//    //==업로드 이미지 조회(만약 프로필 이미지가 null 이면 기본 상점 이미지 조회)메서드==//
//    private UploadedImage getUploadedImage(CreateStoreRequestDto requestDto) {
//
//        //업로드 이미지 조회
//        UploadedImage uploadedImage = uploadStoreProfileImageService.findByImageUrl(requestDto.getImageUrl());
//
//        //업로드 이미지 사용 처리
//        uploadedImage.usedTrue();
//
//        //만약 업로드한 상점 이미지가 없다면 기본 이미지로 설정
//        if(uploadedImage == null) {
//            uploadedImage=uploadStoreProfileImageService.findDefaultProfileImage();
//        }
//        return uploadedImage;
//    }

    /**
     * [서비스 로직]
     * ==수정할 상점이 로그인한 회원의 상점인지 확인(회원의 것이 아니면 예외를 던짐)
     * @param store 상점 엔티티
     * @param member 회원 엔티티
     */
    public void validateStoreOwner(Store store, Member member) {
        if(!store.getMember().getId().equals(member.getId())) {
            throw new StoreCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==상점 업데이트 실행 로직 메서드==//
    private void processUpdate(UpdateStoreRequestDto requestDto, Store store) {

        //기존의 업로드 이미지(상점 프로필 이미지) 비사용 처리
        oldStoreImageUsedFalse(store);

        //새로운 업로드 이미지(상점 프로필 이미지) 사용 처리 + 반환
        UploadedImage newUploadedImage = newStoreImageUsedTrueAndGet(requestDto);

        //새로운 업로드 이미지 URL 로 상점 프로필 이미지 객체 생성
        StoreProfileImage storeProfileImage = StoreProfileImage.create(newUploadedImage.getImageUrl());

        //상점 업데이트
        store.update(requestDto.getName(), requestDto.getIntroduction(), storeProfileImage);
    }

    //==새로운 업로드 이미지(상점 프로필 이미지) 사용 처리 + 반환 메서드==//
    private UploadedImage newStoreImageUsedTrueAndGet(UpdateStoreRequestDto requestDto) {
        //새로운 업로드 이미지 조회
        UploadedImage newUploadedImage = uploadStoreProfileImageService.findByImageUrl(requestDto.getImageUrl());

        //업로드 이미지 사용 처리
        newUploadedImage.usedTrue();
        return newUploadedImage;
    }

    //==기존의 업로드 이미지(상점 프로필 이미지) 비사용 처리 메서드==//
    private void oldStoreImageUsedFalse(Store store) {
        //기존의 업로드 이미지 조회
        UploadedImage oldUploadedImage = uploadStoreProfileImageService.findByImageUrl(store.getStoreProfileImage().getImageUrl());

        //기본(디폴트) 상점 이미지 조회
        UploadedImage defaultStoreImage = uploadStoreProfileImageService.findDefaultProfileImage();

        //기존의 업로드 이미지가 (기본)디폴트 이미지가 아니라면 이미지 비사용 처리
        if(!oldUploadedImage.getImageUrl().equals(defaultStoreImage.getImageUrl())) {
            oldUploadedImage.usedFalse();
        }
    }


}
