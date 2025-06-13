package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
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




}
