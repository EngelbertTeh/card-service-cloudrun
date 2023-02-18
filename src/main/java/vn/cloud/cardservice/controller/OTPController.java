package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        if(email != null) {
            InternalMessenger<OTP> internalMessenger = otpService.generateOTP(email);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED);
        }
            else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    @PutMapping("/update")
    public ResponseEntity<OTP> updateOTP(@RequestBody OTP otpOther) {
        if(otpOther != null){
            InternalMessenger<OTP> internalMessenger = otpService.updateOTP(otpOther);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
            }
            else return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteOTP(@PathVariable Long id) {
        if(id != null){
            boolean isDeleted = otpService.deleteOTPById(id);
            if(isDeleted) {
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            else return new ResponseEntity<>(false,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


