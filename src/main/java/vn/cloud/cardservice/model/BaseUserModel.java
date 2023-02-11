package vn.cloud.cardservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import vn.cloud.cardservice.validator.NoFunnyNames;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@MappedSuperclass
public abstract class BaseUserModel {

	@NotBlank
	@NoFunnyNames
	@Email
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9]+\\..+$")
	@Column(unique = true)
	private String email;
	
	@NotBlank
    @Size(min = 8, max = 255)
	@Column(nullable=false)
    private String password;
	
	
	@NotNull
	@JsonIgnore
	@Column(nullable=false)
    private Boolean isDeactivated = false;
	
	@NotNull
	@TimeZoneStorage(TimeZoneStorageType.NATIVE)
	@JsonIgnore
	@Setter(AccessLevel.NONE)
    @Column(nullable=false)
    private ZonedDateTime createdAt = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Singapore"));

	@Override
	public String toString() {
		return "BaseUserModel{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				", isDeactivated=" + isDeactivated +
				", createdAt=" + createdAt +
				'}';
	}
}
