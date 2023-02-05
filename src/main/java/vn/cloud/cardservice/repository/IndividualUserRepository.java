package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.IndividualUser;

import java.util.Optional;

public interface IndividualUserRepository extends JpaRepository<IndividualUser,Long>{
    Optional<IndividualUser> findByEmail(String email);
}
