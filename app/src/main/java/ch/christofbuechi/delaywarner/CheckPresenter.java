package ch.christofbuechi.delaywarner;

import java.util.List;

import javax.inject.Inject;

import ch.christofbuechi.delaywarner.base.BasePresenter;
import ch.christofbuechi.delaywarner.network.TransportService;
import ch.christofbuechi.delaywarner.network.model.Station;
import ch.christofbuechi.delaywarner.network.model.StationWrapper;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
public class CheckPresenter extends BasePresenter<CheckUI> {

    @Inject
    TransportService transportService;


    @Inject
    public CheckPresenter() {
    }

    public void callService() {
        transportService.getStations("47.3768866", "8.541694000000007").subscribe(new Consumer<StationWrapper>() {
            @Override
            public void accept(StationWrapper stationWrapperResponse) throws Exception {
                // TODO: 07.01.2017 handle response
                List<Station> stations = stationWrapperResponse.getStations();
                Timber.d("List of Statins: " + stations.size());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.d(throwable);
                // TODO: 07.01.2017 error happened
            }
        });
        // TODO call server...
    }
}
