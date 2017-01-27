package ch.christofbuechi.delaywarner;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.text.DateFormat;
import java.util.Date;
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
public class CheckPresenter extends BasePresenter<CheckUI> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    @Inject
    TransportService transportService;
    private String mLastUpdateTime;


    @Inject
    public CheckPresenter() {
    }


    public void callService(Location location) {

//        transportService.getStations("47.3768866", "8.541694000000007").subscribe(new Consumer<StationWrapper>() {
        transportService.getStations(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())).subscribe(new Consumer<StationWrapper>() {
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


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ?
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getUi().showErrorcase();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        getUi().updateUI(location, mLastUpdateTime);
        callService(location);

    }

}
