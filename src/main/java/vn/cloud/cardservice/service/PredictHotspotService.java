package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.PredictionAccuracy;
import vn.cloud.cardservice.repository.PredictHotspotRepository;


@Service
public class PredictHotspotService {
    @Autowired
    WebClient predictHotspotWebClient;

    @Autowired
    PredictHotspotRepository predictHotspotRepository;


    public InternalMessenger<String> predictToday() {
        try {
            String location = predictHotspotWebClient.get()
                    .uri("api/predict-hotspot/predict")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return new InternalMessenger<>(location, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>("error", false, "flask server error");
        }
    }

    public InternalMessenger<PredictionAccuracy> saveAccuracy(Double accuracy) {
        try {
            PredictionAccuracy predictionAccuracy  = new PredictionAccuracy();
            predictionAccuracy.setAccuracy(accuracy);
            PredictionAccuracy predictionAccuracyR = predictHotspotRepository.saveAndFlush(predictionAccuracy);
            return new InternalMessenger<>(predictionAccuracyR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, "Unable to save");
        }


    }
}
