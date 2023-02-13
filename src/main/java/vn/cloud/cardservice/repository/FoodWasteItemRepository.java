package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cloud.cardservice.model.FoodWasteItem;

import java.util.List;
import java.util.Optional;

public interface FoodWasteItemRepository extends JpaRepository<FoodWasteItem,Long> {
    @Query("SELECT fwi FROM FoodWasteItem fwi JOIN fwi.businessUser biz WHERE biz.id = :bizId")
    List<FoodWasteItem> findAllFoodWasteItemsByBusinessUserId(@Param("bizId")Long bizId);

    @Query("SELECT fwi FROM FoodWasteItem fwi WHERE LOWER(fwi.name) = :name")
    Optional<FoodWasteItem> findByName(@Param("name") String name);
}
