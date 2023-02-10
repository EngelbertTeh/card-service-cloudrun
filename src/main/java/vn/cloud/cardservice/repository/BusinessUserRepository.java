package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.cloud.cardservice.model.BusinessUser;

import java.util.List;
import java.util.Optional;

public interface BusinessUserRepository extends JpaRepository<BusinessUser,Long> {
    Optional<BusinessUser> findByEmail(String email);
    @Query("SELECT biz FROM BusinessUser biz WHERE biz.isDeactivated = FALSE")
    List<BusinessUser> findAllActive();
}
