
package ch.christofbuechi.delaywarner.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionWrapper {

    @SerializedName("connections")
    @Expose
    private List<Connection> connections = null;

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

}
