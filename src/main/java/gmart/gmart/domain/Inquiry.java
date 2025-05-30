package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.AnswerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


/**
 * 문의
 * - 문의 테이블은 회원에 의해서만 유일하게 관리되기 때문에 CASCADE와 고아객체를 이용해
 *   회원 쪽에서 생명주기를 관리
 */
@Entity
@Table(name = "inquiry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("내용")
    @Column(name = "content", nullable = false)
    private String content;

    @Comment("답변")
    @Column(name = "answer")
    private String answer;

    @Comment("답변 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status", nullable = false)
    private AnswerStatus answerStatus;

    /**
     * 생성 메서드
     * @param member 회원
     * @param title 제목
     * @param content 내용
     * @return 문의 엔티티
     */
    public static Inquiry createEntity(Member member, String title, String content){
        Inquiry inquiry = new Inquiry();
        inquiry.title = title;
        inquiry.content = content;
        inquiry.answerStatus=AnswerStatus.UNANSWERED;
        inquiry.setMember(member); // 연관관계 세팅 포함
        return inquiry;
    }

    /**
     * 연관관계편의메서드
     * 문의-회원 세팅
     */
     public void setMember(Member member){
        this.member = member;
        member.getInquiries().add(this);
    }

    /**
     * 업데이트 로직
     */
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    /**
     * [비즈니스 로직]
     * 문의에 답변 등록
     * 답변 상태  -> ANSWERED
     */
    public void creteAnswer(String answer){
        this.answer = answer;
        this.answerStatus=AnswerStatus.ANSWERED;
    }

    /**
     * [비즈니스 로직]
     * 문의 답변 삭제
     * 답변 상태 -> UNANSWERED
     */
    public void deleteAnswer(){
        this.answer=null;
        this.answerStatus=AnswerStatus.UNANSWERED;
    }


}
