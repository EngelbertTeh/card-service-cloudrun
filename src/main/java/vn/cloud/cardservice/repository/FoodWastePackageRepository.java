package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cloud.cardservice.model.FoodWastePackage;

import java.util.List;

public interface FoodWastePackageRepository extends JpaRepository<FoodWastePackage,Long> {
    @Query("SELECT p FROM FoodWastePackage p WHERE p.id = :id")
    List<FoodWastePackage> findFoodWastePackageByBusinessUserId(@Param("id") Long id);

}
