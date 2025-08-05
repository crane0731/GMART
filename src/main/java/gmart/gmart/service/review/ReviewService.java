package gmart.gmart.service.review;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.Review;
import gmart.gmart.domain.Store;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.OrderStatus;
import gmart.gmart.domain.enums.ReviewType;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.dto.review.*;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ReviewCustomException;
import gmart.gmart.repository.order.OrderRepository;
import gmart.gmart.repository.review.ReviewRepository;
import gmart.gmart.service.member.MemberService;
import gmart.gmart.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 리뷰 서비스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final MemberService memberService; //회원 서비스
    private final OrderService orderService;//주문 서비스

    private final ReviewRepository reviewRepository; //리뷰 레파지토리

    /**
     * [서비스 로직]
     * 리뷰 등록
     * @param requestDto 리뷰 등록 요청 DTO
     */
    @Transactional
    public void createReview(CreateReviewRequestDto requestDto) {

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = orderService.findById(requestDto.getOrderId());

        //리뷰 등록을 위한 검증 로직
        validateCreateReview(member, order);

        //리뷰 생성 로직
        processCreateReview(requestDto, order, member);

    }

    /**
     * [서비스 로직]
     * 리뷰 논리적 삭제 (SOFT DELETE)
     * @param reviewId 리뷰 아이디
     */
    @Transactional
    public void softDelete(Long reviewId){

        //현재 로그인한 회원 조회(구매자)
        Member member = memberService.findBySecurityContextHolder();

        //리뷰 조회
        Review review = findById(reviewId);

        //현재 로그인한 회원이 리뷰의 작성자인지 확인
        validateReviewOwner(member, review);

        //리뷰 논리적 삭제 처리
        review.softDelete();

    }

    /**
     * [서비스 로직]
     * 리뷰 상세 조회
     * @param reviewId 리뷰 아이디
     * @return ReviewResponseDto 응답 DTO
     */
    public ReviewDetailsResponseDto getReviewDetails(Long reviewId){

        //리뷰 조회
        Review review = findOne(reviewId);

        //조회한 리뷰가 삭제된 상태인지 확인
        validateReviewDeleted(review);

        //응답 DTO 생성 + 반환
        return ReviewDetailsResponseDto.create(review);
    }

    /**
     * [서비스 로직]
     * 주문 아이디(PK)로 리뷰 상세 조회
     * @param orderId 주문 아이디
     * @return ReviewDetailsResponseDto
     */
    public ReviewDetailsResponseDto getReviewDetailsByOrder(Long orderId){
        
        //주문 아이디(PK)을 통해 리뷰를 조회
        Review review = findByOrderId(orderId);

        //조회한 리뷰가 삭제된 상태인지 확인
        validateReviewDeleted(review);

        //응답 DTO 생성 + 반환
        return ReviewDetailsResponseDto.create(review);

    }


    /**
     * [서비스 로직]
     * 자신의 리뷰 조회
     * @param condDto 검색 조건 DTO
     * @return PagedReviewListResponseDto
     */
    public PagedReviewListResponseDto getMyReviews(SearchReviewCondDto condDto,int page){

        //현재 로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //페이징 된 리뷰 리스트 조회
        Page<Review> pageList = reviewRepository.findByCondByMember(member, condDto, createPageable(page));

        //좋아요 리뷰 수
        Long goodCount = reviewRepository.countReviewsByReviewType(member, ReviewType.GOOD);

        //별로에요 리뷰 수
        Long badCount = reviewRepository.countReviewsByReviewType(member, ReviewType.BAD);

        //받은 리뷰 총 평균
        Long ratingAverage = reviewRepository.totalReviewsByMember(member);

        //응답 리뷰 리스트 DTO  생성
        List<ReviewListResponseDto> content = pageList.getContent().stream().map(ReviewListResponseDto::create).toList();

        //페이징 응답 DTO 생성 + 반환
        return PagedReviewListResponseDto.create(goodCount,badCount,ratingAverage,content,pageList.getNumber(),
                pageList.getSize(), pageList.getTotalPages(), pageList.getTotalElements(), pageList.isFirst(), pageList.isLast());
    }
    

    /**
     * [서비스 로직]
     * 특정 회원이 받은 리뷰 목록 조회
     * @param memberId 회원 아이디
     * @param condDto 검색 조건 DTO
     * @return PagedResponseDto<ReviewListResponseDto> 페이징된 응답 DTO 리스트
     */
    public PagedReviewListResponseDto getReviews(Long memberId,SearchReviewCondDto condDto,int page){

        //회원 조회
        Member member = memberService.findById(memberId);

        //페이징 된 리뷰 리스트 조회
        Page<Review> pageList = reviewRepository.findByCondByMember(member, condDto, createPageable(page));

        //좋아요 리뷰 수
        Long goodCount = reviewRepository.countReviewsByReviewType(member, ReviewType.GOOD);

        //별로에요 리뷰 수
        Long badCount = reviewRepository.countReviewsByReviewType(member, ReviewType.BAD);

        //받은 리뷰 총 평균
        Long ratingAverage = reviewRepository.totalReviewsByMember(member);

        //응답 리뷰 리스트 DTO  생성
        List<ReviewListResponseDto> content = pageList.getContent().stream().map(ReviewListResponseDto::create).toList();

        //페이징 응답 DTO 생성 + 반환
        return PagedReviewListResponseDto.create(goodCount,badCount,ratingAverage,content,pageList.getNumber(),
                pageList.getSize(), pageList.getTotalPages(), pageList.getTotalElements(), pageList.isFirst(), pageList.isLast());

    }

    /**
     * [저장]
     * @param review 리뷰 엔티티
     */
    @Transactional
    public void save(Review review) {
        reviewRepository.save(review);
    }

    /**
     * [조회]
     * ID (PK) 값으로 단일 조회
     * @param id 리뷰 아이디
     * @return Review 리뷰 엔티티
     */
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(()-> new ReviewCustomException(ErrorMessage.NOT_FOUND_REVIEW));
    }

    /**
     * [조회 : 패치조인]
     * ID (PK) 값으로 단일 조회(패치 조인)
     * @param id 리뷰 아이디
     * @return Review 리뷰 엔티티
     */
    public Review findOne(Long id) {
        return reviewRepository.findOne(id).orElseThrow(()-> new ReviewCustomException(ErrorMessage.NOT_FOUND_REVIEW));
    }


    //==리뷰를 쓰려는 회원이 해당 주문의 구매자인지 확인하는 로직==//
    private void validateOrderBuyer(Member member, Order order) {
        if(!member.getId().equals(order.getBuyer().getId())) {
            throw new ReviewCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==주문 아이디(PK)을 통해 리뷰를 조회==//
    private Review findByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId).orElseThrow(() -> new ReviewCustomException(ErrorMessage.NOT_FOUND_REVIEW));
    }

    //==리뷰 생성 로직==//
    private void processCreateReview(CreateReviewRequestDto requestDto, Order order, Member buyer) {
        //주문의 판매자 조회
        Member seller = order.getSeller();

        //판매자의 상점 조회
        Store store = seller.getStore();

        //이미 리뷰가 존재하는지 확인, 만약 삭제 상태라면 복구하고, 삭제상태가 아니라면 에러를 던짐
        if (validateExistsReview(order, buyer, seller)) return;

        //리뷰 생성
        Review review = Review.create(buyer, seller, order, requestDto.getContent(), requestDto.getReviewType(), requestDto.getRating());

        //판매자의 리뷰 받은 수 증가
        seller.plusReviewedCount();

        //판매자 상점의 리뷰 받은 수 증가
        store.plusReviewedCount();

        //리뷰 저장
        save(review);
    }

    //==이미 리뷰가 존재하는지 확인하는 로직==//
    private boolean validateExistsReview(Order order, Member buyer, Member seller) {
        //이미 작성한 리뷰가 있는지 조회
        Review existsReview = reviewRepository.findByReviewerAndRevieweeAndOrder(buyer, seller, order).orElse(null);

        if(existsReview != null) {
            if(existsReview.getDeleteStatus().equals(DeleteStatus.DELETED)) {
                existsReview.recovery();
                return true;
            }
            else if(existsReview.getDeleteStatus().equals(DeleteStatus.UNDELETED)){
                throw new ReviewCustomException(ErrorMessage.ALREADY_REVIEW);
            }
        }
        return false;
    }

    //==주문이 구매확정 상태인지 확인 -> 구매 확정 상태여야 리뷰를 등록 할 수 있음==//
    private void validateOrderCompleted(Order order) {
        if(!order.getOrderStatus().equals(OrderStatus.CONFIRM)){
            throw new ReviewCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==리뷰 등록을 위한 검증 로직==//
    private void validateCreateReview(Member member, Order order) {
        //리뷰를 쓰려는 회원이 해당 주문의 구매자인지 확인하는 로직
        validateOrderBuyer(member, order);

        //주문이 구매확정 상태인지 확인 -> 구매 확정 상태여야 리뷰를 등록 할 수 있음
        validateOrderCompleted(order);
    }

    //==현재 로그인한 회원이 리뷰의 작성자인지 확인 하는 로직==//
    private void validateReviewOwner(Member member, Review review) {
        if(!member.getId().equals(review.getReviewer().getId())){
            throw new ReviewCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==조회한 리뷰가 삭제된 상태인지 확인하는 로직==//
    private void validateReviewDeleted(Review review) {
        if(review.getDeleteStatus().equals(DeleteStatus.DELETED)){
            throw new ReviewCustomException(ErrorMessage.NOT_FOUND_REVIEW);
        }
    }

    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        Pageable pageable = PageRequest.of(page, 5); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==페이징 응답 DTO 생성 메서드==//
    private PagedResponseDto<ReviewListResponseDto> createPagedResponseDto(List<ReviewListResponseDto> content, Page<Review> page) {
        return PagedResponseDto.<ReviewListResponseDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

}
