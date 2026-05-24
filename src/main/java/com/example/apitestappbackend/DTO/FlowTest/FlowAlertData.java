package com.example.apitestappbackend.DTO.FlowTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowAlertData {
    @JsonProperty("alert_id")
    private String alertId;

    @JsonProperty("blocked_edge")
    private String blockedEdge;
}
