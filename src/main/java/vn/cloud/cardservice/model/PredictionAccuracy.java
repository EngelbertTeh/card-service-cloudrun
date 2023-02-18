package vn.cloud.cardservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
public class PredictionAccuracy {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(unique=true)
    private Long id;

    @Column(name = "date_today", nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime dateToday;

    @Column(updatable = false)
    private Double accuracy;

}
