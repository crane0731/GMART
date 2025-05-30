package gmart.gmart.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 댓글 응답 DTO
 */
@Getter
@Setter
public class CommentResponseDto {

    private Long memberId; //회원 아이디
    private String nickname; //닉네임
    private String loginId; //로그인 아이디(이메일)
    private String mannerGrade; //매너 등급
    private String memberRole; //회원 권한

    private Long commentId; //댓글 아이디
    private String content; //댓글 내용

    private List<CommentResponseDto> children; //자식 댓글

    private String createdAt; //생성일
    private String updatedAt; //수정일



}
