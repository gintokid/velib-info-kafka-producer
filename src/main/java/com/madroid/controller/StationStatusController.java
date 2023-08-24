package com.madroid.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madroid.model.StationState;
import com.madroid.repository.StationStateRepository;
import com.madroid.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@CrossOrigin
@RestController
@RequestMapping("/api/getStationsStatus")
public class StationStatusController {

    @Autowired
    StationStateRepository repository;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");


    @GetMapping
    public ResponseEntity<?> getStations() {
        try {

            Date now = new Date();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            System.out.println("###################################################################");
            System.out.println("TRY : " + now);
            System.out.println("###################################################################");


            URL url = new URL("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/station_status.json");
            StringBuffer content = HttpUtil.getUrl(url);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode respNode = objectMapper.readTree(content.toString());
            JsonNode stations = respNode.get("data").get("stations");
            for (JsonNode stationNode : stations) {
                createStationStatus(stationNode, now, timestamp);
            }

            TimeUnit.SECONDS.sleep(15 * 60);
            //     }
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void createStationStatus(JsonNode stationNode, Date now, Timestamp timestamp) {

        StationState stationStatus = new StationState();
        stationStatus.setStationId(stationNode.get("station_id").asLong());

        stationStatus.setIsInstalled(stationNode.get("is_installed").asBoolean());
        stationStatus.setIsRenting(stationNode.get("is_renting").asBoolean());
        stationStatus.setIsReturning(stationNode.get("is_returning").asBoolean());
        stationStatus.setNumMechBikesAvailable(stationNode.get("num_bikes_available_types").get(0).get("mechanical").asLong());
        stationStatus.setNumElecBikesAvailable(stationNode.get("num_bikes_available_types").get(1).get("ebike").asLong());
        stationStatus.setNumDocksAvailable(stationNode.get("num_docks_available").asLong());

        stationStatus.setStateDate(now);
        stationStatus.setTimestamp(timestamp);

        System.out.println("stationStatus --> date :" + now.toString() + "  -  " + stationStatus);

        repository.save(stationStatus);
    }


}
