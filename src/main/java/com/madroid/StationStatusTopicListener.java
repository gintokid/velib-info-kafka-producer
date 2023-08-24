package com.madroid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madroid.model.StationState;
import com.madroid.repository.StationStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class StationStatusTopicListener {

    @Autowired
    StationStateRepository repository;

    @KafkaListener(topics = "velibdata-station-status", groupId = "velibdata")
    void consume(String data) throws JsonProcessingException {
        System.out.println("Listener received data :" + data.length() + "characters... 👍");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode respNode = objectMapper.readTree(data);
        JsonNode stationStateNodes = respNode.get("data").get("stations");
        List<StationState> states = new ArrayList<>();

        Date now = new Date();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        for (JsonNode stationStateNode : stationStateNodes) {

            StationState stationStatus = StationState.builder()
                    .stationId(stationStateNode.get("station_id").asLong())
                    .isInstalled(stationStateNode.get("is_installed").asBoolean())
                    .isRenting(stationStateNode.get("is_renting").asBoolean())
                    .isReturning(stationStateNode.get("is_returning").asBoolean())
                    .numElecBikesAvailable(stationStateNode.get("num_bikes_available_types").get(0).get("mechanical").asLong())
                    .numMechBikesAvailable(stationStateNode.get("num_bikes_available_types").get(1).get("ebike").asLong())
                    .numDocksAvailable(stationStateNode.get("num_docks_available").asLong())
                    .stateDate(now)
                    .timestamp(timestamp)
                    .build();

            states.add(stationStatus);
        }

        repository.saveAll(states);


    }
}
