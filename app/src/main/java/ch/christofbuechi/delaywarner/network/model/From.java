
package ch.christofbuechi.delaywarner.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class From {

    @SerializedName("arrival")
    @Expose
    private Object arrival;
    @SerializedName("arrivalTimestamp")
    @Expose
    private Object arrivalTimestamp;
    @SerializedName("departure")
    @Expose
    private String departure;
    @SerializedName("departureTimestamp")
    @Expose
    private Integer departureTimestamp;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("prognosis")
    @Expose
    private Prognosis prognosis;
    @SerializedName("station")
    @Expose
    private Station station;

    public Object getArrival() {
        return arrival;
    }

    public void setArrival(Object arrival) {
        this.arrival = arrival;
    }

    public Object getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(Object arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Integer getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(Integer departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Prognosis getPrognosis() {
        return prognosis;
    }

    public void setPrognosis(Prognosis prognosis) {
        this.prognosis = prognosis;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

}
