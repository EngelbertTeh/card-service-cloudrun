package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vn.cloud.cardservice.dto.GeoCodingResponse;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/hotspot")
public class PredictHotspotController {

    @Autowired
    WebClient geoCodingWebClient;
    @Value("${sm://GMAP_API_KEY}")
    String APIKEY; // in google cloud secrets
    String postalCode = "602288";


    @GetMapping("/get/longlat")
    public GeoCodingResponse geoCoding() {




        return geoCodingWebClient.get()
                .uri(builder -> builder.path("geocode/json").queryParam("components", "postal_code:%s",postalCode).queryParam("key",APIKEY).build())
                .header("Accept", "application/json")
                .exchangeToMono(response->{
                    if (response.statusCode().is2xxSuccessful()){
                        return response.bodyToMono(GeoCodingResponse.class);
                    }
                    return response.createException().flatMap(Mono::error);
                }).block();
    }
}
