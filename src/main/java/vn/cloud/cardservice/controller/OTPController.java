package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.dto.InternalMessenger;
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
        if (email != null) {
            InternalMessenger<OTP> internalMessenger = otpService.getOTPByEmail(email);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
            }
            else if(!internalMessenger.getErrorMessage().equals("not found")){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // if it is due to server error
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if email is not connected to any OTP or email is null
    }


    // Update


    // Delete
    // Not required in controller for now


}