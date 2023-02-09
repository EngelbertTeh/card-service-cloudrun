package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.FoodWastePackage;

public interface FoodWastePackageRepository extends JpaRepository<FoodWastePackage,Long> {
}
