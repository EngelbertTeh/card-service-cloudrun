package vn.cloud.cardservice.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;

@Component
public class ScheduledTasks {

    @Autowired
    WebClient predictHotspotWebClient;
    @Scheduled(cron = "0 0 0 * * *") // runs everyday at 12 midnight
    public void scrapeDataTrainModel() {
        try {
            scrapeOlioWebsite();
            combineBaselineData();
            trainPredictionModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrapeOlioWebsite() throws ServiceUnavailableException {
        Boolean hasScraped = false;
        Integer count = 0;
        while(!hasScraped && count < 5) { // keeps on calling flask server to scrape for 5 times
            hasScraped = predictHotspotWebClient.get()
                    .uri("api/predict-hotspot/scrape")
                    .exchangeToMono(response->{
                        if (response.statusCode().is2xxSuccessful()){
                            return Mono.just(true); // returns true if manage to scrape
                        }
                        else return Mono.just(false); // if unable to scrape at flask server
                    }).block();
            count++;
        }
        if(!hasScraped) {
            throw new ServiceUnavailableException("Flask server unable to provide service to scrape Olio webpage");
        }
    }

    private void combineBaselineData() throws ServiceUnavailableException {
        Boolean hasCombined = false;
        Integer count = 0;
        while(!hasCombined && count < 5) { // keeps on calling flask server to combine datasets for 5 times
            hasCombined = predictHotspotWebClient.get()
                    .uri("api/predict-hotspot/combine")
                    .exchangeToMono(response->{
                        if (response.statusCode().is2xxSuccessful()){
                            return Mono.just(true); // returns true if manage to combine
                        }
                        else return Mono.just(false); // if unable to combine scraped data into the overall baseline dataset at flask server
                    }).block();
            count++;
        }
        if(!hasCombined) {
            throw new ServiceUnavailableException("Flask server unable to provide service to combine dataset");
        }

    }

    private void trainPredictionModel() throws ServiceUnavailableException {
        Boolean hasTrained = false;
        Integer count = 0;
        while(!hasTrained && count < 5) { // keeps on calling flask server to train model with new data for 5 times
            hasTrained = predictHotspotWebClient.get()
                    .uri("api/predict-hotspot/train")
                    .exchangeToMono(response->{
                        if (response.statusCode().is2xxSuccessful()){
                            return Mono.just(true); // returns true if manage to train model
                        }
                        else return Mono.just(false); // if unable to train model at flask server
                    }).block();
            count++;
        }
        if(!hasTrained) {
            throw new ServiceUnavailableException("Flask server unable to provide service to train prediction model");
        }

    }

}
