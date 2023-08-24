package com.madroid.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "station_state")
public class StationState {


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(name = "station_id", nullable = false)
    Long stationId;

    @Column(name = "state_date", nullable = false)
    Date stateDate;

    @Column(name = "state_timestamp")
    Timestamp timestamp;

    @Column(name = "nb_mechanical_available")
    Long numMechBikesAvailable;

    @Column(name = "nb_ebike_available")
    Long numElecBikesAvailable;

    @Column(name = "nb_docks_available")
    Long numDocksAvailable;

    @Column(name = "is_renting")
    Boolean isRenting;

    @Column(name = "is_installed")
    Boolean isInstalled;

    @Column(name = "is_returning")
    Boolean isReturning;

    @Override
    public String toString() {
       return "id : " + stationId + " - available : " + numMechBikesAvailable + "/" + numElecBikesAvailable + " - freeDocks :" + numDocksAvailable;
    }
}