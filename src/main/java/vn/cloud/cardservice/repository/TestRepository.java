package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.Test;


public interface TestRepository extends JpaRepository<Test,Long> {

}
