package gmart.gmart.repository.comment;

import gmart.gmart.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 댓글 레파지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 게시글 아이디로 댓글 찾기
     * @param id 게시글 아이디
     * @return  List<Comment> 댓글 리스트
     */
    List<Comment> findByArticleId(Long articleId);

}
