package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cloud.cardservice.model.Food;

import java.time.ZonedDateTime;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Long> {
    @Query("SELECT f FROM Food f WHERE (LOWER(f.title) LIKE %:title% AND LOWER(f.halalStatus) = :halalStatus)")
    List<Food> findFoodsByCriteria(@Param("title") String title, @Param("halalStatus") String halalStatus);

    @Query("SELECT f FROM Food f JOIN f.individualUser ind WHERE ind.id = :id AND f.isCollected = :isCollected")
    List<Food> findFoodsByIndividualUserIdAndCollectedStatus(@Param("id") Long id, @Param("isCollected") Boolean isCollected);

    @Query("SELECT f FROM Food f WHERE LOWER(f.halalStatus) = :halalStatus") // query not really needed, but i wanted to lower case the halal status to be extra safe
    List<Food> findAllByHalalStatus(@Param("halalStatus") String halalStatus);

    @Query("SELECT f FROM Food f WHERE LOWER(f.title) LIKE %:title%")
    List<Food> findAllByTitle(@Param("title") String title);

    @Query("SELECT f FROM Food f WHERE f.createdAt BETWEEN :begin AND :end")
    List<Food> findAllByDate(@Param("begin") ZonedDateTime begin, @Param("end") ZonedDateTime end );
}
