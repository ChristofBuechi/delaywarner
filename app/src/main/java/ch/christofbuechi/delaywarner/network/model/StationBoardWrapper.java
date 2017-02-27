package ch.christofbuechi.delaywarner.network.model;

import java.util.List;

/**
 * Created by Christof on 27.02.2017.
 */
public class StationBoardWrapper {
    public Station station;
    private List<StationBoard> stationboard = null;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public List<StationBoard> getStationboard() {
        return stationboard;
    }

    public void setStationboard(List<StationBoard> stationboard) {
        this.stationboard = stationboard;
    }
}
