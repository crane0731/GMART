package gmart.gmart.exception;

/**
 * 게시글 관련 사용자 에러
 */
public class ArticleCustomException extends RuntimeException {


    public ArticleCustomException(String message) {
        super(message);
    }

    public ArticleCustomException() {
        super();
    }

    public ArticleCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleCustomException(Throwable cause) {
        super(cause);
    }
}
