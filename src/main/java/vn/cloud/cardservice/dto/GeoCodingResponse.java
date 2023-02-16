package vn.cloud.cardservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoCodingResponse {
    @JsonProperty("results")
    private GeoCodingResult[] geoCodingResults;


}
