package ch.christofbuechi.delaywarner.network;

import ch.christofbuechi.delaywarner.network.model.StationWrapper;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Christof on 07.01.2017.
 */

public interface TransportServiceDefinition {
    @GET("locations")
    Observable<Response<StationWrapper>> listLocations(@Query("query") String user);

    @GET("locations")
    Observable<Response<StationWrapper>> listLocations(@Query("x") String xCoordinates, @Query("y") String yCoordinates);

    @GET("connections")
    Observable<Response<StationWrapper>> listConnections(@Query("from") String fromLocation, @Query("to") String toLocation);
}
