package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.FoodWaste;

public interface FoodWasteRepository extends JpaRepository<FoodWaste,Long> {
}
