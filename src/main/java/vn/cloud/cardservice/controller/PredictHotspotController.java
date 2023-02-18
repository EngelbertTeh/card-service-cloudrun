package vn.cloud.cardservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.service.PredictHotspotService;

@RestController
@RequestMapping("/api/hotspot")
public class PredictHotspotController {

    @Autowired
    PredictHotspotService predictHotspotService;

    @GetMapping("/predict")
    public ResponseEntity<String> predictHotspot() {

        InternalMessenger<String> internalMessenger = predictHotspotService.predictToday();
        if(internalMessenger.isSuccess()) {
            return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
