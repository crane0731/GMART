package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 댓글
 */
@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("게시글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;


    @org.hibernate.annotations.Comment("부모 댓글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children=new ArrayList<>();


    @org.hibernate.annotations.Comment("댓글 내용")
    @Column(name = "content")
    private String content;


    /**
     * [생성 메서드]
     * 일반 댓글
     * @param member 회원
     * @param article 게시글
     * @param content 댓글 내용
     * @return Comment 엔티티
     */
    public static Comment createComment(Member member, Article article, String content) {

        Comment comment = new Comment();

        comment.setMember(member);
        comment.setArticle(article);

        comment.content = content;

        return comment;

    }

    /**
     * [생성 메서드]
     * 대댓글
     * @param parent 부모 댓글
     * @param member 회원
     * @param article 게시글
     * @param content 댓글 내용
     * @return Comment 엔티티
     */
    public static Comment createChildComment(Comment parent,Member member, Article article,String content) {
        Comment comment = new Comment();

        comment.setParent(parent);
        comment.setMember(member);
        comment.setArticle(article);
        comment.content = content;

        return comment;
    }

    public void update(String content){
        this.content = content;
    }


    /**
     * [연관 관계 편의 메서드]
     * Comment - Member
     * @param member 회원
     */
    private void setMember(Member member) {
        this.member = member;
        member.getComments().add(this);
    }

    /**
     * [연관 관계 편의 메서드]
     * Comment - Article
     * @param article 게시글
     */
    private void setArticle(Article article) {
        this.article = article;
        article.getComments().add(this);
    }

    /**
     * [연관 관계 편의 메서드]
     * Comment-Parent
     * @param parent 부모 댓글
     */
    private void setParent(Comment parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }


}
