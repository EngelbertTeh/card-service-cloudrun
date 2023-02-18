package vn.cloud.cardservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class OTP {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @Column(unique=true)
    private Long id;
    @NonNull
    private Integer oneTimePasswordCode;
    @JsonIgnore
    @Column(name = "expired_time", nullable = false, updatable = false)
    @NotNull
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime expiredTime;

    @JsonIgnore
    @Column(name = "created_time", nullable = false, updatable = false)
    @NotNull
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime createdTime;

    @NotBlank
    private String email;
}