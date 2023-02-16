package vn.cloud.cardservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoCodingCoordinates {
    @JsonProperty("location")
    private GeoCodingLocation geoCodingLocation;
}
