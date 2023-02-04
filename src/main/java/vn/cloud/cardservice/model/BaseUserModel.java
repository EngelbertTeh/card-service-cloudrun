package vn.cloud.cardservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import vn.cloud.cardservice.validator.NoFunnyNames;

import java.time.ZonedDateTime;

@Data
@MappedSuperclass
public abstract class BaseUserModel {

	@NotBlank
	@NoFunnyNames
	@Email
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9]+\\..+$")
	@Column(unique = true, nullable = false)
	private String email;
	
	@NotBlank
    @Size(min = 8, max = 30)
	@Column(nullable=false)
    private String password;
	
	
	@NotNull
	@Column(nullable=false)
    private Boolean isDeactivated = false;
	
	@NotNull
	@Setter(AccessLevel.NONE)
    @Column(nullable=false)
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
