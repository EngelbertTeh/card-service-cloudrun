package vn.cloud.cardservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebClientConfig {


    @Bean
    public WebClient predictHotspotWebClient(WebClient.Builder builder ){

        return builder.baseUrl("https://getprediction-lwbna3zk2a-et.a.run.app/").build();
    }

    @Bean
    public WebClient geoCodingWebClient(){

        return WebClient.create("https://maps.googleapis.com/maps/api/");

    }


}
