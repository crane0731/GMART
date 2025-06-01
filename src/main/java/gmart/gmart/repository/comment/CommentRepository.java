package gmart.gmart.repository.comment;

import gmart.gmart.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 댓글 레파지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
