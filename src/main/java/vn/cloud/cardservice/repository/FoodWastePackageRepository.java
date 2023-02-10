package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cloud.cardservice.model.FoodWastePackage;

import java.util.List;

public interface FoodWastePackageRepository extends JpaRepository<FoodWastePackage,Long> {
    @Query("SELECT fwp FROM FoodWastePackage fwp JOIN fwp.businessUser biz WHERE biz.id = :id AND fwp.isCollected = FALSE AND fwp.isDeactivated = FALSE")
    List<FoodWastePackage> findAllNotCollectedFoodWastePackagesByBusinessUserId(@Param("id") Long id);

    @Query("SELECT fwp FROM FoodWastePackage fwp JOIN fwp.businessUser biz WHERE biz.id = :id AND (fwp.isCollected  OR fwp.isDeactivated = TRUE)")
    List<FoodWastePackage> findAllFoodWastePackagesHistoryByBusinessUserId(@Param("id") Long id);

    @Query("SELECT fwp FROM FoodWastePackage fwp WHERE fwp.isDeactivated = FALSE")
    List<FoodWastePackage> findAllActive();

}
