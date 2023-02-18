package vn.cloud.cardservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@Data
@Entity
@Table(name="food_waste_package")
public class FoodWastePackage {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // prevent manually setting of Id using lombok, all Id must be generated by the database to maintain integrity
    @Column(unique=true)
	private Long id;
    
    private String packageName;

    private Integer quantity;


    private LocalTime startTime;

    private LocalTime endTime;

    @FutureOrPresent
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate pickUpDate;

    @JsonIgnore
    private Boolean isCollected = false;

    private String status;

    @JsonIgnore
    private Boolean isDeactivated = false;
    private String description;

    private String itemList;

    @JsonProperty("business")
    @ManyToOne
    private BusinessUser businessUser;

}
