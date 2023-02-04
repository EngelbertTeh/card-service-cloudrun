package vn.cloud.cardservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true) //include attributes from superclass in the equals and hashcode methods
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class BusinessUser extends BaseUserModel
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String category = "Corporate User"; // don't include this in db
    private String businessName;
    private String branch;
    private String businessType;
    private String address;
    private String postalCode;
    private String contactNumber;
    private String openingDays;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime openingTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime closingTime;


}

