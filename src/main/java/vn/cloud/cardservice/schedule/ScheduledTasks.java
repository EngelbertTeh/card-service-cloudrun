package vn.cloud.cardservice.schedule;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vn.cloud.cardservice.model.Food;
import vn.cloud.cardservice.repository.FoodRepository;

import javax.naming.ServiceUnavailableException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    WebClient predictHotspotWebClient;
    @Scheduled(cron = "0 36 13 * * *") // runs everyday at 12 midnight
    public void scrapeDataTrainModel() {
        try {
            scrapeDataFromOlioWebsite();
            sendDataFromFoodPostings();
//            combineAllDataSets();
//            trainPredictionModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrapeDataFromOlioWebsite() throws ServiceUnavailableException {
        Boolean hasScraped = false;
        int count = 0;
        while(Boolean.FALSE.equals(hasScraped) && count < 5 ) { // keeps on calling flask server to scrape for 5 times
                hasScraped = predictHotspotWebClient.get()
                        .uri("api/predict-hotspot/scrape")
                        .exchangeToMono(response->{
                            if (response.statusCode().is2xxSuccessful()){
                                return Mono.just(true); // returns true if manage to scrape
                            }
                            else return Mono.just(false); // if unable to scrape at flask server
                        }).block();
                count++;
                System.out.println(count);
            }

        if(Boolean.FALSE.equals(hasScraped)) {
            throw new ServiceUnavailableException("Flask server unable to provide service to scrape Olio webpage");
        }
    }


    private void sendDataFromFoodPostings() throws CsvException, ServiceUnavailableException  {
        Boolean hasSentData = false;
        int count = 0;

        // Set the CSV headers
        String[] headers = {"name", "created_time", "latitude", "longitude"};

        // Get food name ,created_time,latitude,longitude of the previous day
        LocalTime localTimeStart = LocalTime.of(0,0,0);
        LocalTime localTimeEnd = LocalTime.of(23,59,59);
        LocalDate localDateYesterday = LocalDate.now().minusDays(1);
        ZonedDateTime zdtStart = ZonedDateTime.of(localDateYesterday,localTimeStart,ZoneId.of("Asia/Singapore"));
        ZonedDateTime zdtEnd = ZonedDateTime.of(localDateYesterday,localTimeEnd,ZoneId.of("Asia/Singapore"));
        List<Food> foods = foodRepository.findAllByDate(zdtStart,zdtEnd); // making sure that even if there's a delay, the food data to be retrieved should start from the previous day at 00:00:00 hrs

        StringWriter stringWriter = new StringWriter(); // Create a StringWriter to hold the CSV data

        try {
            CSVWriter writer = new CSVWriter(stringWriter);
            writer.writeNext(headers); // Write the headers to the CSV file

            // Loop through the food and write each row to the CSV file
            for (Food food : foods) {
                String[] rowData = {food.getTitle(), String.valueOf(food.getCreatedAt()), String.valueOf(food.getLatitude()), String.valueOf(food.getLongitude())};
                writer.writeNext(rowData);
            }
        } catch(Exception e) {
            throw new CsvException("Unable to write data");
        }

        String csvData = stringWriter.toString(); // Get the CSV data as a string

        while(Boolean.FALSE.equals(hasSentData) && count < 5) { // keeps trying to send csv data for 5 times
            hasSentData = predictHotspotWebClient.post()
                    .uri("api/predict-hotspot/incoming_csv")
                    .body(Mono.just(csvData),String.class)
                    .exchangeToMono(response->{
                        if (response.statusCode().is2xxSuccessful()){
                            return Mono.just(true); // returns true if manage to send
                        }
                        else return Mono.just(false); // if unable to send data
                    }).block();
            count++;
        }

        if(Boolean.FALSE.equals(hasSentData)) {
            throw new ServiceUnavailableException("Flask server unable to receive data");
        }
    }

    private void combineAllDataSets() throws ServiceUnavailableException {
        Boolean hasCombined = false;
        int count = 0;
        while(Boolean.FALSE.equals(hasCombined) && count < 5) { // keeps on calling flask server to combine datasets for 5 times
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
        if(Boolean.FALSE.equals(hasCombined)) {
            throw new ServiceUnavailableException("Flask server unable to provide service to combine dataset");
        }

    }

    private void trainPredictionModel() throws ServiceUnavailableException {
        Boolean hasTrained = false;
        int count = 0;
        while(Boolean.FALSE.equals(hasTrained) && count < 5) { // keeps on calling flask server to train model with new data for 5 times
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
        if(Boolean.FALSE.equals(hasTrained)) {
            throw new ServiceUnavailableException("Flask server unable to provide service to train prediction model");
        }

    }

}
