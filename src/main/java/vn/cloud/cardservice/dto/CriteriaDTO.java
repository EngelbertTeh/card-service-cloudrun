package vn.cloud.cardservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CriteriaDTO {
    @JsonProperty("status")
    private String halalStatus;

    @JsonProperty("search")
    private String title;
}
