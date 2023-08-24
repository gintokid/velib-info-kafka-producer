package com.madroid.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madroid.model.Station;
import com.madroid.repository.StationRepository;
import com.madroid.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;


@CrossOrigin
@RestController
@RequestMapping("/api/getStationsLocation")
public class StationLocationController {

    @Autowired
    StationRepository repository;

    @GetMapping
    public ResponseEntity<?> getStations() {
        try {
            URL url = new URL("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/station_information.json");
            StringBuffer content = HttpUtil.getUrl(url);


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode respNode = objectMapper.readTree(content.toString());
            JsonNode stations = respNode.get("data").get("stations");
            for (JsonNode stationNonde : stations) {
                createStationFromJson(stationNonde);
            }

            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createStationFromJson(JsonNode stationNode) {
        Station station = new Station();
        station.setId(stationNode.get("station_id").asLong());

        station.setName(stationNode.get("name").asText());
        station.setCapacity(stationNode.get("capacity").asLong());
        station.setCode(stationNode.get("stationCode").asText());

        station.setLongitude(stationNode.get("lon").asDouble());
        station.setLatitude(stationNode.get("lat").asDouble());


        System.out.println("station " + station.getId() + " (" + station.getCode() + ") --> + " + station.getName() + " created !");

        repository.save(station);


    }

}
