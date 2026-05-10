package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.MapTest.MapTestData;
import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.repository.MapTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class MapTestService {
    private final MapTestRepository mapTestRepository;

    public MapTestService(MapTestRepository mapTestRepository) {
        this.mapTestRepository = mapTestRepository;
    }

    public boolean isImageURLValid(String url) {
        String regex = "^(https?:\\/\\/)?([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?\\.(jpg|jpeg|png|gif|webp|svg|bmp|ico)(\\?.*)?$";
        return url != null && url.matches(regex);
    }

    public MapTestResponse mapTest(MapTestRequest request) {
        MapTest savedMt;

        try {
            if (request.getBuildingCode().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getBuildingName().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getScaleX().toString().isBlank() || request.getScaleY().toString().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getImageUrl().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (!isImageURLValid(request.getImageUrl())) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Hinh anh khong hop le")
                        .usedInTest(false)
                        .build();
            }

            if (request.getScaleX().isNaN() || request.getScaleY().isNaN()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message(ResponseCode.INVALID_VALUE.getMessage())
                        .usedInTest(false)
                        .build();
            }

            MapTest mt = new MapTest();
            mt.setBuildingCode(request.getBuildingCode().trim());
            mt.setBuildingName(request.getBuildingName().trim());
            mt.setImageUrl(request.getImageUrl().trim());
            mt.setScaleX(Double.parseDouble(request.getScaleX().toString()));
            mt.setScaleY(Double.parseDouble(request.getScaleY().toString()));
            mt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            mt.setCode(ResponseCode.SUCCESS.getCode());
            mt.setMessage(ResponseCode.SUCCESS.getMessage());
            mt.setUsedInTest(false);

            savedMt = mapTestRepository.save(mt);

            return MapTestResponse.builder()
                    .timestamp(savedMt.getTimeStamp())
                    .status(savedMt.getStatus())
                    .code(savedMt.getCode())
                    .message(savedMt.getMessage())
                    .usedInTest(savedMt.getUsedInTest())
                    .data(new MapTestData(
                            savedMt.getId(),
                            savedMt.getBuildingCode(),
                            savedMt.getBuildingName(),
                            savedMt.getImageUrl(),
                            savedMt.getScaleX(),
                            savedMt.getScaleY()
                    ))
                    .build();
        } catch (Exception e) {
            log.error("Error mapping test: ", e);
            return MapTestResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }

    }
}
