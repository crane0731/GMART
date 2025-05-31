package gmart.gmart.dto.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 정렬 기준
 */
public enum ArticleSortType {

    COMMENT_HIGH, //댓글 높은 순
    COMMENT_LOW, // 댓글 낮은 순

    VIEW_HIGH, //조회 수 높은 순
    VIEW_LOW, //조회 수 낮은 순

    LIKE_HIGH, //좋아요 수 높은 순
    LIKE_LOW, //좋아요 수 낮은 순

    CREATE_DATE_ASC,  //예전 글 부터
    CREATE_DATE_DESC // 최신 글 부터

}
