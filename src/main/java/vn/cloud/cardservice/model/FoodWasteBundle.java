package vn.cloud.cardservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;



@NoArgsConstructor
@Data
@Entity
public class FoodWasteBundle {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    private String packageName;

    private int quantity;

    private LocalTime start;

    private LocalTime end;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate pickUpDate;

    private String status = "Pending";

    private String description;

    @ManyToOne
    private BusinessUser businessUser;

}
