package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.Dummy;

import java.util.Optional;

public interface DummyRepository extends JpaRepository<Dummy,Long> {
    Optional<Dummy> findByEmail(String email);
}
