package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.cloud.cardservice.model.PredictionAccuracy;

public interface PredictHotspotRepository extends JpaRepository<PredictionAccuracy,Long> {
}
