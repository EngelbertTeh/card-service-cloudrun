package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.model.OTP;
import vn.cloud.cardservice.repository.OTPRepository;
import vn.cloud.cardservice.utils.OTPGeneratorUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class OTPService {

    @Autowired
    OTPRepository otpRepository;


    public OTP returnOTP(String email) {
        OTP otp = new OTP();

        otp.setOneTimePasswordCode(OTPGeneratorUtil.createOTP().get());

        ZonedDateTime creationTimeStamp = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
        ZonedDateTime expiryTimeStamp = ZonedDateTime.now(ZoneId.of("Asia/Singapore")).plusMinutes(3);
        otp.setCreatedTime(creationTimeStamp);
        otp.setCreatedTime(expiryTimeStamp);
        otp.setEmail(email);

        return otpRepository.saveAndFlush(otp);
    }
}
