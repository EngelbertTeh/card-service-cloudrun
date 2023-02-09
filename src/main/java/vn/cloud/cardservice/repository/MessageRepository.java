package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
