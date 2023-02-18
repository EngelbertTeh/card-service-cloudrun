package vn.cloud.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.cloud.cardservice.model.OTP;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    @Query("SELECT otp FROM OTP otp WHERE LOWER(otp.email) = :email AND otp.expiredTime < :currentTime")
    Optional<OTP> getUnexpiredOTPByEmail(@Param("email") String email, @Param("currentTime")ZonedDateTime currentTime);

    @Modifying
    @Query("DELETE FROM OTP otp WHERE LOWER(otp.email) = :email")
    void deleteByEmail(@Param("email") String email);
}