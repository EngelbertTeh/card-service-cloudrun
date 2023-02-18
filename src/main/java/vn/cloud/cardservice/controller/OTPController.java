package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.model.OTP;
import vn.cloud.cardservice.service.BusinessUserService;
import vn.cloud.cardservice.service.OTPService;

@RestController
@RequestMapping("/api/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private BusinessUserService businessUserService;


    // Create
    @GetMapping("/generate/{email}")
    private ResponseEntity<OTP> generateOTP(@PathVariable("email") String email) {
        try {
            return new ResponseEntity<>(otpService.generateOTP(email),HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Retrieve
    @GetMapping("/retrieve/{email}")
    public ResponseEntity<OTP> getOTPByEmail(@PathVariable("email") String email){
        OTP otp = otpService.retrieveOTPByEmail(email);
        if(otp != null) {
            return new ResponseEntity<>(otp, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    // Update


    // Delete
    // Not required in controller for now


}