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


    @PostMapping("/generateOTP/{email}")
    @ResponseBody
    private Object getOneTimePassword(@PathVariable("email") String email) {
        try {
            return ResponseEntity.ok(otpService.returnOTP(email));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }

    /////////////////////////////////For OTP///////////////////////////////////////////////////

    @GetMapping("/retrieve/{email}")
    public ResponseEntity<OTP> getOTPByEmail(@PathVariable("email") String email){
        OTP otp = businessUserService.retrieveOTPByEmail(email);
        return new ResponseEntity<>(otp, HttpStatus.OK);
    }


}