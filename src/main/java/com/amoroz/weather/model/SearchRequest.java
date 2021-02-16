package com.amoroz.weather.model;

import lombok.Data;
import com.amoroz.weather.validator.SearchRequestCheck;

@Data
@SearchRequestCheck
public class SearchRequest {

    private String userName = "anonymous";
    private Integer cityId;
    private String cityName;
    private Double latitude;
    private Double longitude;
}
