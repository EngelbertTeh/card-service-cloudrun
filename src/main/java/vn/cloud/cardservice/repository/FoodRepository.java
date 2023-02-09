package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.Food;

public interface FoodRepository extends JpaRepository<Food,Long> {
}
