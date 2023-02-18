package vn.cloud.cardservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FoodWasteItem implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // prevent manually setting of Id using lombok, all Id must be generated by the database to maintain integrity
    @Column(unique=true)
	private Long id;

    private String name;

    private String category;

    private Integer period;

    private String manufacturer;

    private Double weight;

    @JsonProperty("img")
    private String imageUrl;

    @JsonProperty("business")
    @ManyToOne
    private BusinessUser businessUser;

    @NotNull
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Singapore"));





}
