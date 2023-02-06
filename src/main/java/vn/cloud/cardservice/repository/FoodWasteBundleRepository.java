package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.FoodWasteBundle;

public interface FoodWasteBundleRepository extends JpaRepository<FoodWasteBundle,Long> {
}
