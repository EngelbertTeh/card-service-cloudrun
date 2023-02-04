package vn.cloud.cardservice.model;


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
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Setter(AccessLevel.NONE) // prevent manually setting of Id using lombok, all Id must be generated by the database to maintain integrity
	 @Column(unique=true)
	 private Long id;
	 
	 @NoFunnyNames
	 @NotBlank
	 @Size(min = 6, max=50)
	 @Pattern(regexp="^\\D*$") //no digits
	 @Column(nullable=false)
	 private String name; 
	 
	 @Nullable
	 @Pattern(regexp="^\\+[0-9]+[0-9]+") //country code and phone number
	 @Column(nullable=true)
	 private String phone;

	 @Min(value = 0)
	 @Column(nullable=false)
	 private Double salary = 0.00;

	 @Column(nullable=false)
	 private String level;

	 @Min(value=0)
	 @Column(nullable=false)
	 private Integer points = 0;

	 @DateTimeFormat(pattern = "dd/MM/yyyy")
	 @Past
	 @Nullable
	 @Column(nullable=true)
	 private LocalDate birth;




//	 @NotNull
//	 @Column(nullable=false)
//	 private Integer postcode;
}
