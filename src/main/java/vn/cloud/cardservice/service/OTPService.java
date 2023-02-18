package vn.cloud.cardservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.OTP;
import vn.cloud.cardservice.repository.OTPRepository;
import vn.cloud.cardservice.utils.OTPGeneratorUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OTPService {

    @Autowired
    OTPRepository otpRepository;

    // Create
    @Transactional
    public InternalMessenger<OTP> generateOTP(String email) {
        try {
            String emailT = email.toLowerCase().trim();

            otpRepository.deleteByEmail(email.toLowerCase().trim()); // delete previous otp first

            OTP otp = new OTP();

            otp.setOneTimePasswordCode(OTPGeneratorUtil.createOTP().get());

            ZonedDateTime creationTimeStamp = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Singapore"));
            ZonedDateTime expiryTimeStamp = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Singapore")).plusMinutes(5);
            otp.setCreatedTime(creationTimeStamp);
            otp.setExpiredTime(expiryTimeStamp);
            otp.setEmail(emailT);
            OTP otpS = otpRepository.saveAndFlush(otp);
            return new InternalMessenger<>(otpS,true);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }


    // Retrieve
    public InternalMessenger<OTP> getOTPByEmail (String email) {
        try {
            String emailT = email.toLowerCase().trim();
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
            Optional<OTP> otpOptional = otpRepository.getUnexpiredOTPByEmail(emailT,currentTime);
            if(otpOptional.isPresent()) {
                return new InternalMessenger<>(otpOptional.get(),true);
            }
            return new InternalMessenger<>(null,false,"not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }

    }

    // Update
    public InternalMessenger<OTP> updateOTP(OTP otpOther) {
        try {
            Optional<OTP> otpOpt = otpRepository.findById(otpOther.getId());
            if (otpOpt.isPresent()) { 
                OTP otpR = otpRepository.saveAndFlush(otpOther); // save changes
                return new InternalMessenger<>(otpR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    // Delete
    public Boolean deleteOTPById(Long id) { // hard delete
        try {
            Optional<OTP> otpOpt = otpRepository.findById(id);
            if (otpOpt.isPresent()) { // make sure otp exists
                OTP otpR = otpOpt.get();
                otpRepository.delete(otpR);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
