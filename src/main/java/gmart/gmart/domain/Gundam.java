package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.GundamGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 건담 정보
 */
@Entity
@Table(name = "gundam")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gundam extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("건담 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gundam_id")
    private Long id;

    @org.hibernate.annotations.Comment("이름")
    @Column(name = "name")
    private String name;

    @org.hibernate.annotations.Comment("건담 등급")
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private GundamGrade grade;

    @org.hibernate.annotations.Comment("건담 이미지 URL")
    @Column(name = "image_url")
    private String imageUrl;

}
