package com.example.apitestappbackend.DTO.FlowTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowDensityData {
    @JsonProperty("current_people")
    private Integer currentPeople;
}
