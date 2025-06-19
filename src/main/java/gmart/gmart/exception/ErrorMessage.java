package gmart.gmart.exception;

/**
 * 커스텀 에러 메시지
 */
public final class ErrorMessage {

    public static final String NOT_FOUND_MEMBER = "회원을 찾을 수 없습니다.";
    public static final String NOT_FOUND_REFRESH_TOKEN = "리프레쉬 토큰을 찾을 수 없습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    public static final String DUPLICATED_LOGIN_ID = "로그인 아이디가 이미 존재합니다.";
    public static final String DUPLICATED_NICKNAME = "닉네임이 이미 존재합니다.";
    public static final String DUPLICATED_PHONE = "전화번호가 이미 존재합니다.";
    public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
    public static final String FAILED_CREATED_DIRECTORY = "디렉토리 생성에 실패하였습니다.";
    public static final String INCORRECT_FILE_NAME = "올바른 파일 이름이 아닙니다.";
    public static final String NOT_IMAGE_FILE = "이미지 파일이 아닙니다.";
    public static final String FAILED_IMAGE_UPLOAD = "이미지 업로드에 실패하였습니다.";
    public static final String FAILED_DELETE_FILE = "파일 삭제에 실패했습니다.";
    public static final String NOT_FOUND_FILE = "파일을 찾을 수 없습니다.";
    public static final String EXPIRED_TOKEN = "토큰이 만료되었습니다.";
    public static final String LOGOUT_TOKEN = "로그아웃 된 토큰입니다.";
    public static final String INCORRECT_PASSWORD = "잘못된 비밀번호입니다.";
    public static final String ALREADY_SUSPENDED_MEMBER = "이미 정지된 회원입니다.";
    public static final String NO_PERMISSION = "권한이 없습니다.";


    public static final String NOT_FOUND_INQUIRY = "문의를 찾을 수 없습니다.";
    public static final String ALREADY_ANSWER ="이미 답변을 완료했습니다.";
    public static final String NOT_FOUND_ANSWER="답변을 찾을 수 없습니다.";


    public static final String NOT_FOUND_GUNDAM="건담 정보를 찾을 수 없습니다.";

    public static final String NOT_FOUND_FAVORITE_GUNDAM="관심 건담을 찾을 수 없습니다.";
    public static final String ALREADY_FAVORITE_GUNDAM="이미 관심 등록한 건담 입니다.";

    public static final String NOT_ENOUGH_GMONEY_TO_REFUND="환불 하려는 금액이 현재 가진 건머니 보다 많습니다.";

    public static final String NOT_FOUND_GPOINT_CHARGE_LOG="건포인트 충전 로그를 찾을 수 없습니다.";
    public static final String NOT_FOUND_GMONEY_CHARGE_LOG="건머니 충전 로그를 찾을 수 없습니다.";

    public static final String NOT_FOUND_STORE="상점을 찾을 수 없습니다.";

    public static final String NOT_FOUND_LIKE_STORE="상점 좋아요를 찾을 수 없습니다.";
    public static final String ALREADY_LIKE_STORE="이미 좋아요한 상점 입니다.";

    public static final String NOT_FOUND_FAVORITE_STORE="관심 상점을 찾을 수 없습니다.";
    public static final String ALREADY_FAVORITE_STORE="이미 관심등록한 상점 입니다.";

    public static final String NOT_FOUND_ITEM="상품을 찾을 수 없습니다.";

    public static final String NOT_FOUND_FAVORITE_ITEM="관심 상품을 찾을 수 없습니다.";
    public static final String ALREADY_FAVORITE_ITEM="이미 관심등록한 상품 입니다.";


    public static final String NOT_ITEM_SELLER="해당 상품의 판매자가 아닙니다.";
    public static final String NOT_FOUND_REPORT ="신고를 찾을 수 없습니다";
    public static final String ALREADY_REPORT="이미 신고등록이 되었습니다.";
    public static final String SELF_REPORT_NOT_ALLOWED = "자신은 신고할 수 없습니다.";

    public static final String NOT_FOUND_ADMIN_MESSAGE="관리자 메시지를 찾을 수 없습니다.";

    public static final String POINT_MINIMUM_REQUIRED="포인트는 1000원 이상 부터 사용 가능합니다.";
    public static final String POINT_EXCEEDS_TOTAL ="사용하려는 포인트가 결제 금액보다 많습니다.";
    public static final String NOT_FOUND_ORDER ="주문을 찾을 수 없습니다.";
    public static final String CANNOT_PURCHASE_OWN_ITEM ="자신이 등록한 상품은 구매할 수 없습니다.";
    public static final String ALREADY_RESERVED_ORDER = "이미 예약한 주문이 있습니다.";

    public static final String NOT_ENOUGH_GMONEY="건머니가 부족합니다.";
    public static final String INVALID_GMONEY_DEDUCTION_AMOUNT="차감할 금액은 0보다 커야합니다.";

    public static final String NOT_ENOUGH_GPOINT="건포인트가 부족합니다.";

    public static final String CANNOT_CONFIRM_ORDER ="주문을 수락 처리 할 수 없습니다.";
    public static final String CANNOT_CANCEL_ORDER ="주문을 취소 처리 할 수 없습니다.";
    public static final String CANNOT_SHIP_DELIVERY ="배송을 설정할 수 없습니다.";
    public static final String CANNOT_CANCEL_READY_DELIVERY = "배송 준비완료를 취소할 수 없습니다.";

    public static final String ALREADY_CANCEL_ORDER="이미 취소된 주문입니다.";







}