package gmart.gmart.domain.baseentity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseAuditingEntity {

    @Comment("생성일")
    @CreatedDate
    @Column(name = "created_date",updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @Comment("수정일")
    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Comment("생성자")
    @CreatedBy
    @Column(name = "creator_id" , updatable = false,nullable = false)
    private Long creator;

    @Comment("수정자")
    @LastModifiedBy
    @Column(name = "modifier_id",nullable = false)
    private Long modifier;

}
