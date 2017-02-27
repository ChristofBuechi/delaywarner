package ch.christofbuechi.delaywarner.network;


import ch.christofbuechi.delaywarner.base.BaseService;
import ch.christofbuechi.delaywarner.network.model.Station;
import ch.christofbuechi.delaywarner.network.model.StationBoardWrapper;
import ch.christofbuechi.delaywarner.network.model.StationWrapper;
import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by Christof on 07.01.2017.
 */
public class TransportService extends BaseService {


    private final TransportServiceDefinition transportServiceDefinition;


    public TransportService(Retrofit retrofit, SchedulerWrapper schedulerWrapper) {
        super(retrofit, schedulerWrapper);
        this.transportServiceDefinition = createServiceDefinition(retrofit);
    }

    private TransportServiceDefinition createServiceDefinition(Retrofit retrofit) {
        return retrofit.create(TransportServiceDefinition.class);
    }


    public Observable<StationWrapper> getStations(String latitude, String longitude) {
       return transportServiceDefinition.listLocations(latitude, longitude)
               .flatMap((response) -> applyHTTPErrorHandling(response))
               .compose((observable) -> applySchedulers(observable));
    }

    public Observable<StationBoardWrapper> getTimeTable(Station station) {
        return transportServiceDefinition.getStationBoardFromId(station.getId(), "1")
               .flatMap((response) -> applyHTTPErrorHandling(response))
               .compose((observable) -> applySchedulers(observable));
    }
}
