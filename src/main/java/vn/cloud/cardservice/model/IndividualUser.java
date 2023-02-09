package vn.cloud.cardservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.cloud.cardservice.validator.NoFunnyNames;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true) //include attributes from superclass in the equals and hashcode methods
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class IndividualUser extends BaseUserModel{



	 @JsonProperty("userId")
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Setter(AccessLevel.NONE) // prevent manually setting of Id using lombok, all Id must be generated by the database to maintain integrity
	 @Column(unique=true)
	 private Long id;


	 @NoFunnyNames
	 @NotBlank
	 @Size(min = 6, max=50)
	 @Pattern(regexp="^[a-zA-Z0-9]+$") //no space
	 @Column(nullable=false)
	 private String userName;
	 
	 @Nullable
	 @Pattern(regexp="^\\+[0-9]+[0-9]+") //country code and phone number
	 private String phone;

	 @Min(value = 0)
	 @Column(nullable=false)
	 private Double salary = 0.00;

	 @NotBlank
	 @Column(nullable=false)
	 private String role;

	 @Column(nullable=true)
	 private String level;

	 @Min(value=0)
	 @Column(nullable=true)
	 private Integer points = 0;

	 @DateTimeFormat(pattern = "dd/MM/yyyy")
	 @Past
	 @Nullable
	 @Column(nullable=true)
	 private LocalDate birth;

}
