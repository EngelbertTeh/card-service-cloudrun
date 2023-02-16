package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class PredictHotspotService {
    @Autowired
    WebClient predictHotspotWebClient;

    public Boolean manualScrape() {
       return predictHotspotWebClient.get()
                .uri("api/predict-hotspot/scrape")
                .exchangeToMono(response->{
                    if (response.statusCode().is2xxSuccessful()){
                        return Mono.just(true); // returns true if manage to scrape
                    }
                    else return Mono.just(false); // if unable to scrape at flask server
                }).block();
    }
}
