package vn.cloud.cardservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vn.cloud.cardservice.dto.GeoCodingResponse;
import vn.cloud.cardservice.model.Food;

@Component
public class Postcode2LocationUtil {

    @Autowired
    WebClient geoCodingWebClient;

    @Value("${gmaps.apikey}")
    private String APIKEY;


    public Food convertPostcode2Loc(Food foodOther) {
        try {
            String postalCode = foodOther.getPostcode();

            GeoCodingResponse geoCodingResponse =  geoCodingWebClient.get()
                    .uri(builder -> builder.path("geocode/json").queryParam("components", String.format("country:%s|postal_code:%s","SG",postalCode)).queryParam("key",APIKEY).build())
                    .header("Accept", "application/json")
                    .exchangeToMono(response->{
                        if (response.statusCode().is2xxSuccessful()){
                            return response.bodyToMono(GeoCodingResponse.class);
                        }
                        return response.createException().flatMap(Mono::error);
                    }).block();

            if (geoCodingResponse.getGeoCodingResults().length == 0) { // no results
                throw new IllegalArgumentException();
            }

            Double latitude = geoCodingResponse.getGeoCodingResults()[0].getCoordinates().getGeoCodingLocation().getLat();
            Double longitude = geoCodingResponse.getGeoCodingResults()[0].getCoordinates().getGeoCodingLocation().getLng();


            foodOther.setLatitude(latitude);
            foodOther.setLongitude(longitude);

            return foodOther;
        } catch(Exception e) {
            e.printStackTrace();
            return foodOther; // if there's an issue using google maps geocoding to convert the postcode, just return back the food without lat and lng for now until the developer see what's causing the issue
        }

    }
}
