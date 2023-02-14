package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cloud.cardservice.model.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Long> {
    @Query("SELECT f FROM Food f WHERE (LOWER(f.title) LIKE %:title% AND LOWER(f.halal) = :halal)")
    List<Food> findAllByCriteria(@Param("title") String title, @Param("halal") String halal);

    @Query("SELECT f FROM Food f WHERE LOWER(f.halal) = :halal")
    List<Food> findAllByHalalStatus(@Param("halal") String halal);

    @Query("SELECT f FROM Food f JOIN f.individualUser ind WHERE ind.id = :id AND f.isCollected = :isCollected")
    List<Food> findFoodsByIndividualUserIdAndCollectedStatus(@Param("id") Long id,@Param("isCollected") Boolean isCollected);
}
