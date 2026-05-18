package com.example.apitestappbackend.DTO.SavedSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedSearchRequest {
    private Object keyword;

    @JsonProperty("user_id")
    private String userId;
}
