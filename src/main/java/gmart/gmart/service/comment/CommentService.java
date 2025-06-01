package gmart.gmart.service.comment;


import gmart.gmart.domain.Article;
import gmart.gmart.domain.Comment;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.comment.CreateCommentRequestDto;
import gmart.gmart.dto.comment.UpdateCommentRequestDto;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.repository.comment.CommentRepository;
import gmart.gmart.service.article.ArticleService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final MemberService memberService; //회원 서비스
    private final ArticleService articleService; //게시글 서비스

    private final CommentRepository commentRepository;//댓글 레파지토리

    /**
     * [서비스 로직]
     * 댓글(대댓글) 등록
     * @param articleId 게시글 아이디
     * @param requestDto 게시글 등록 요청 DTO
     */
    @Transactional
    public void createComment(Long articleId,CreateCommentRequestDto requestDto) {

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //댓글 등록을 할 게시글을 조회
        Article article = articleService.findById(articleId);


        //만약 부모 댓글이 있다면 (등록하려는 댓글이 대댓글 이라면)
        if(requestDto.getParentCommentId()!=null){
            //대댓글 등록
            saveChildComment(requestDto, member, article);

        }else{
            //댓글 등록
            saveParentComment(Comment.createComment(member, article, requestDto.getContent()));
        }
    }

    /**
     * [서비스 로직]
     * 댓글 수정
     * @param commentId 댓글 아이디
     * @param requestDto 댓글 수정 요청 DTO
     */
    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequestDto requestDto){

        //로그인한 회원 조회
        Member member = memberService.findBySecurityContextHolder();

        //수정할 댓글 조회
        Comment comment = findById(commentId);

        //댓글 수정
        comment.update(requestDto.getContent());
    }

    /**
     * 댓글 삭제
     */

    /**
     * 댓글 조회(리스트)
     */

    /**
     * [저장]
     * @param comment 댓글
     */
    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * [조회]
     * 아이디(PK) 값으로 조회
     * @param id 댓글 아이디
     * @return Comment 엔티티
     */
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(()->new ArticleCustomException(ErrorMessage.NOT_FOUND_COMMENT));
    }



    //==부모 댓글 생성+저장 메서드==//
    private void saveParentComment(Comment member) {

        //댓글 생성
        Comment comment = member;
        //댓글 저장
        commentRepository.save(comment);
    }

    //==대댓글 생성 + 저장 메서드==//
    private void saveChildComment(CreateCommentRequestDto requestDto, Member member, Article article) {
        //부모 댓글 조회
        Comment parent = findById(requestDto.getParentCommentId());

        //부모댓글과 게시글이 일치하는지 확인
        validateParentInArticle(article, parent);

        //대댓글 생성
        Comment child = Comment.createChildComment(parent, member, article, requestDto.getContent());

        //대댓글 저장
        commentRepository.save(child);
    }

    //==부모댓글과 게시글이 일치하는지 확인 하는 메서드==//
    private void validateParentInArticle(Article article, Comment parent) {
        if (!parent.getArticle().getId().equals(article.getId())) {
            throw new ArticleCustomException(ErrorMessage.PARENT_COMMENT_ARTICLE_MISMATCH);
        }
    }


}
