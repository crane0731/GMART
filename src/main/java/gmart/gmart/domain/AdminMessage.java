package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.AdminMessageType;
import gmart.gmart.domain.enums.DeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 관리자 메시지
 */
@Entity
@Table(name = "admin_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminMessage extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("관리자 메시지 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_message_id")
    private Long id;

    @org.hibernate.annotations.Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @org.hibernate.annotations.Comment("내용")
    @Column(name = "content")
    private String content;

    @org.hibernate.annotations.Comment("메시지 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private AdminMessageType messageType;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param content 메시지 내용
     * @param messageType 메시지 타입
     * @return AdminMessage 관리자 메시지 엔티티
     */
    public static AdminMessage create(String content, AdminMessageType messageType) {

        AdminMessage adminMessage = new AdminMessage();

        adminMessage.content = content;
        adminMessage.messageType = messageType;
        adminMessage.deleteStatus = DeleteStatus.UNDELETED;

        return adminMessage;
    }


    /**
     * [연관 관계 편의 메서드]
     * 회원 세팅
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
        member.getAdminMessages().add(this);
    }

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        if(deleteStatus.equals(DeleteStatus.UNDELETED)){
            this.deleteStatus=DeleteStatus.DELETED;

            this.member.minusAdminMessageCount();

        }
    }

    /**
     * [비즈니스 로직]
     * recovery
     */
    public void recovery(){
        if(deleteStatus.equals(DeleteStatus.DELETED)){
            this.deleteStatus=DeleteStatus.UNDELETED;

            this.member.plusAdminMessageCount();
        }
    }


}
