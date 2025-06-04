//package gmart.gmart.domain;
//
//import gmart.gmart.domain.baseentity.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
///**
// * 실시간 채팅 메시지
// */
//@Entity
//@Table(name = "chat_message")
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ChatMessage extends BaseTimeEntity {
//
//    @org.hibernate.annotations.Comment("채팅 메시지 아이디")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_message_id")
//    private Long id;
//
//    @org.hibernate.annotations.Comment("채팅룸 아이디")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_room_id")
//    private ChatRoom chatRoom;
//
//    @org.hibernate.annotations.Comment("보내는 회원 아이디")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_id")
//    private Member sender;
//
//    @org.hibernate.annotations.Comment("받는 회원 아이디")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "receiver_id")
//    private Member receiver;
//
//    @org.hibernate.annotations.Comment("메시지 내용")
//    @Column(name = "message")
//    private String message;
//
//
//
//}
