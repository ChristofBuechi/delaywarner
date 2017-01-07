
package ch.christofbuechi.delaywarner.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prognosis {

    @SerializedName("platform")
    @Expose
    private Object platform;
    @SerializedName("arrival")
    @Expose
    private Object arrival;
    @SerializedName("departure")
    @Expose
    private Object departure;
    @SerializedName("capacity1st")
    @Expose
    private Integer capacity1st;
    @SerializedName("capacity2nd")
    @Expose
    private Integer capacity2nd;

    public Object getPlatform() {
        return platform;
    }

    public void setPlatform(Object platform) {
        this.platform = platform;
    }

    public Object getArrival() {
        return arrival;
    }

    public void setArrival(Object arrival) {
        this.arrival = arrival;
    }

    public Object getDeparture() {
        return departure;
    }

    public void setDeparture(Object departure) {
        this.departure = departure;
    }

    public Integer getCapacity1st() {
        return capacity1st;
    }

    public void setCapacity1st(Integer capacity1st) {
        this.capacity1st = capacity1st;
    }

    public Integer getCapacity2nd() {
        return capacity2nd;
    }

    public void setCapacity2nd(Integer capacity2nd) {
        this.capacity2nd = capacity2nd;
    }

}
