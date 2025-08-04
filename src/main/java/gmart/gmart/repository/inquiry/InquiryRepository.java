package gmart.gmart.repository.inquiry;

import gmart.gmart.domain.Inquiry;
import gmart.gmart.domain.enums.DeleteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 문의 레파지토리
 */
public interface InquiryRepository extends JpaRepository<Inquiry, Long>,InquiryRepositoryCustom {

    @Query( "SELECT i " +
            "FROM Inquiry i " +
            "WHERE i.id=:id and i.deleteStatus=:deleteStatus")
    Optional<Inquiry> findOne(@Param("id")Long id, @Param("deleteStatus") DeleteStatus deleteStatus);
}
