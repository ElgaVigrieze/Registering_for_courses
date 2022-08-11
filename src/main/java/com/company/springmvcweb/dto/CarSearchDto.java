package com.company.springmvcweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarSearchDto {
    @JsonProperty("number")
    private String number;

    @JsonProperty("vin_number")
    private String vinNumber;
}
