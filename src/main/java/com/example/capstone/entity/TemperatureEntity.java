package com.example.capstone.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureEntity {
    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("humidity")
    private String humidity;
}
