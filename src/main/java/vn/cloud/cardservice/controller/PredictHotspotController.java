package vn.cloud.cardservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.schedule.ScheduledTasks;

@RestController
@RequestMapping("/api/hotspot")
public class PredictHotspotController {


    @Autowired
    ScheduledTasks scheduledTasks;


    @GetMapping("/get/longlat")
    public ResponseEntity<HttpStatus> geoCoding() {

        try {
            scheduledTasks.scrapeDataTrainModel();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
