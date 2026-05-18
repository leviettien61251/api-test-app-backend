package com.example.apitestappbackend.DTO.SavedSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedSearchData {
    private Integer id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("target_node_id")
    private Integer targetNodeId;

    private String keyword;

    @JsonProperty("searched_at")
    private Timestamp searchedAt;
}
