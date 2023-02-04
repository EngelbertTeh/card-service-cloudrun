package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.BusinessUser;

import java.util.Optional;

public interface BusinessUserRepository extends JpaRepository<BusinessUser,Long> {
    Optional<BusinessUser> findByEmail(String email);
}
