//package gmart.gmart.domain;
//
//import gmart.gmart.domain.baseentity.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
///**
// * 채팅 룸
// */
//@Entity
//@Table(
//        name = "chat_room",
//        uniqueConstraints = @UniqueConstraint(
//                name = "UK_chat_room_memberA_memberB",
//                columnNames = {"member_a_id", "member_b_id"}
//        )
//)
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ChatRoom extends BaseTimeEntity {
//
//    @org.hibernate.annotations.Comment("채팅룸 아이디")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_room_id")
//    private Long id;
//
//
//    @org.hibernate.annotations.Comment("회원A 아이디")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_a_id")
//    private Member memberA;
//
//    @org.hibernate.annotations.Comment("회원B 아이디")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_b_id")
//    private Member memberB;
//
//}
