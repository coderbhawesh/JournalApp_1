package com.example.JournalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {


    private Current current;
    @Getter
    @Setter
    public class Current{
        @JsonProperty("observation_time")
        public String observationTime;
        public int temperature;
        @JsonProperty("weather_code")
        public int weatherCode;
        @JsonProperty("weather_icons")
        public ArrayList<String> weatherIcons;
        public ArrayList<String> weather_descriptions;
        public int wind_speed;
        public int wind_degree;

        public String is_day;
    }






}
