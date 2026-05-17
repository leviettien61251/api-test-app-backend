package com.example.apitestappbackend.DTO.SavedSearch;

import com.example.apitestappbackend.DTO.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SavedSearchResponse extends Response {
    private SavedSearchData data;
    private Boolean usedInTest;
    private String timestamp;
}
