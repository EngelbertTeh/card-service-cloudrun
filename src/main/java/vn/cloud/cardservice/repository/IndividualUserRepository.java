package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.IndividualUser;

public interface IndividualUserRepository extends JpaRepository<IndividualUser,Long>{

}