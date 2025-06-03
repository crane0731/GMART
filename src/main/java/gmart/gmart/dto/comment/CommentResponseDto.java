package gmart.gmart.dto.comment;

import gmart.gmart.domain.Comment;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 댓글 응답 DTO
 */
@Getter
@Setter
public class CommentResponseDto {

    private Long memberId; //회원 아이디
    private String nickname; //닉네임
    private String memberRole; //회원 권한

    private Long commentId; //댓글 아이디
    private String content; //댓글 내용

    private List<CommentResponseDto> children; //자식 댓글

    private String createdAt; //생성일
    private String updatedAt; //수정일


    /**
     * [생성 메서드] CommentResponseDto
     * Comment -> CommentResponseDto
     * 해당 댓글에 자식 댓글이 있을경우, 생성메서드 재귀 호출을 통하여 똑같이 응답 DTO 를 만들어 리스트에 담는다.
     * @param comment 댓글
     * @return CommentResponseDto 응답 DTO
     */
    public static CommentResponseDto createDto(Comment comment) {

        CommentResponseDto dto = new CommentResponseDto();
        dto.memberId = comment.getMember().getId();
        dto.nickname = comment.getMember().getNickname();
        dto.memberRole=comment.getMember().getMemberRole().toString();

        dto.commentId = comment.getId();
        dto.content = comment.getContent();

        dto.createdAt= DateFormatUtil.DateFormat(comment.getCreatedDate());
        dto.updatedAt= DateFormatUtil.DateFormat(comment.getUpdatedDate());


        //자식 댓글이 있다면 자식댓글들을 담을 응답 DTO 생성
        if (comment.getChildren() != null) {

            //빈 DTO 리스트를 생성
            List<CommentResponseDto> childrenList = new ArrayList<>();

            //자식댓글들
            List<Comment> children = comment.getChildren();

            //리스트에 자식댓글을 추가(재귀 호출)
            for (Comment child : children) {
                CommentResponseDto childDto = createDto(child);
                childrenList.add(childDto);
            }

            dto.children = childrenList;
        }
        //자식 댓글이 없다면 불변 빈 리스트 생성
        else{
            dto.children = Collections.emptyList(); // 불변 빈 리스트로
        }

        return dto;
    }
}
