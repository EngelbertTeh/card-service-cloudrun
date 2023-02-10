package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.cloud.cardservice.model.IndividualUser;

import java.util.List;
import java.util.Optional;

public interface IndividualUserRepository extends JpaRepository<IndividualUser,Long>{
    Optional<IndividualUser> findByEmail(String email);

    @Query("SELECT ind FROM IndividualUser ind WHERE ind.isDeactivated = FALSE")
    List<IndividualUser> findAllActive();
}
