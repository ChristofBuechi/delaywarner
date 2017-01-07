package ch.christofbuechi.delaywarner.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Christof on 07.01.2017.
 */

public class StationWrapper {
    @SerializedName("stations")
    @Expose
    private List<Station> stations = null;

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

}
