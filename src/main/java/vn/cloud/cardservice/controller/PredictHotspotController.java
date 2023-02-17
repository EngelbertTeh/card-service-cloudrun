package vn.cloud.cardservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vn.cloud.cardservice.config.SecretsConfig;
import vn.cloud.cardservice.dto.GeoCodingResponse;

@RestController
@RequestMapping("/api/hotspot")
public class PredictHotspotController {

    @Autowired
    WebClient geoCodingWebClient;

    @Autowired
    SecretsConfig secretsConfig;

    String postalCode = "602288";

    @GetMapping("/get/longlat")
    public GeoCodingResponse geoCoding() {

System.out.println("APIKEY IS " + secretsConfig.apiURL());

        return geoCodingWebClient.get()
                .uri(builder -> builder.path("geocode/json").queryParam("components", "postal_code:%s",postalCode).queryParam("key",secretsConfig.apiURL()).build())
                .header("Accept", "application/json")
                .exchangeToMono(response->{
                    if (response.statusCode().is2xxSuccessful()){
                        return response.bodyToMono(GeoCodingResponse.class);
                    }
                    return response.createException().flatMap(Mono::error);
                }).block();
    }
}
