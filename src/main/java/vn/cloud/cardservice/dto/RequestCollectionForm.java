package vn.cloud.cardservice.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestCollectionForm 
{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String packageName;
    private int quantity = 1;
    private LocalTime start;
    private LocalTime end;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;

    private String status = "Pending";
    private String description;
    private String category;
    private String itemList;
    private long businessId;
    private String manufacturer;


    public RequestCollectionForm (String packageName, int quantity, LocalTime start, LocalTime end, LocalDate pickUpDate, String description, String category, long businessId, String manufacturer){
        this.packageName = packageName;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.pickUpDate = pickUpDate;
        this.description = description;
        this.category = category;
        this.businessId = businessId;
        this.manufacturer = manufacturer;
    }
}
