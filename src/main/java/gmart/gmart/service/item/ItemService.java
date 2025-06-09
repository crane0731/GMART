package gmart.gmart.service.item;

import gmart.gmart.repository.item.ItemRepository;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final MemberService memberService; //회원 서비스

    private final ItemRepository itemRepository;//상품 레파지토리


    /**
     * 상품 등록
     */

    /**
     * 상품 삭제
     */

    /**
     * 상품 수정
     */

    /**
     * 상품 조회
     */

    /**
     * 상품 리스트 조회
     */


}
