package vn.cloud.cardservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoCodingResult {
    @JsonProperty("formatted_address")
    private String address;

    @JsonProperty("geometry")
    private GeoCodingCoordinates coordinates;


}
