package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.Dummy;

public interface DummyRepository extends JpaRepository<Dummy,Long> {
}
