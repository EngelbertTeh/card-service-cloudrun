package vn.cloud.cardservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.model.OTP;
import vn.cloud.cardservice.repository.OTPRepository;
import vn.cloud.cardservice.utils.OTPGeneratorUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class OTPService {

    @Autowired
    OTPRepository otpRepository;

    // Create

    // Retrieve

    public OTP retrieveOTPByEmail (String email) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
        Optional<OTP> otpOptional = otpRepository.getUnexpiredOTPByEmail(email,currentTime);
        if(otpOptional.isPresent()) {
            return otpOptional.get();
        }
        return null;
    }

    // Update

    // Delete




    // Generate OTP
    @Transactional
    public OTP generateOTP(String email) {
        try {
            String emailT = email.toLowerCase().trim();
            otpRepository.deleteByEmail(email.toLowerCase().trim()); // delete previous otp first

            OTP otp = new OTP();

            otp.setOneTimePasswordCode(OTPGeneratorUtil.createOTP().get());

            ZonedDateTime creationTimeStamp = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
            ZonedDateTime expiryTimeStamp = ZonedDateTime.now(ZoneId.of("Asia/Singapore")).plusMinutes(3);
            otp.setCreatedTime(creationTimeStamp);
            otp.setExpiredTime(expiryTimeStamp);
            otp.setEmail(emailT);

            return otpRepository.saveAndFlush(otp);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
