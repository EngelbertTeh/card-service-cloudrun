package vn.cloud.cardservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
public class Food {

    @JsonProperty("foodId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // prevent manually setting of Id using lombok, all Id must be generated by the database to maintain integrity
    @Column(unique=true)
    private Long id;
    private String title;
    private String description;
    private Double listDays;
    private Boolean isPendingPickup = false;
    private Boolean isCollected = false;
    private Boolean isListed = true;
    @JsonProperty("isHalal")
    private String halal;
    private String foodLocation;
    private Double Longitude;
    private Double Latitude;

    @JsonProperty("publisher")
    @ManyToOne
    private IndividualUser individualUser;

    @NotNull
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Column(nullable=false)
    private ZonedDateTime createdAt = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Singapore"));

}
